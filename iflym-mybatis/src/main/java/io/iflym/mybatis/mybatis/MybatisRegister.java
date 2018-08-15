package io.iflym.mybatis.mybatis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.iflym.core.tuple.Tuple2;
import io.iflym.core.util.ExceptionUtils;
import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.criteria.CriteriaSqlSource;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.field.json.Jsoned;
import io.iflym.mybatis.domain.field.json.JsonedTypeHandler;
import io.iflym.mybatis.domain.info.EntityInfo;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.exception.MybatisException;
import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mapperx.util.MappedStatementUtils;
import io.iflym.mybatis.mapperx.util.MapperUtils;
import io.iflym.mybatis.mapperx.util.ResultMapUtils;
import io.iflym.mybatis.mapperx.util.ResultMappingUtils;
import io.iflym.mybatis.mybatis.interceptor.PageInterceptor;
import io.iflym.mybatis.mybatis.interceptor.ResultTypeInterceptor;
import io.iflym.mybatis.part.PartTree;
import io.iflym.mybatis.part.PartTreeSqlSource;
import io.iflym.mybatis.util.TypeUtils;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 往mybatis注册那些没有被实现的方法
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
@Component
@Slf4j
public class MybatisRegister {
    private static final String GENERATED_TAG = "_GENERATED_";

    public static final String MAPPER_NAMESPACE_INTERNAL = "io.iflym.mybatis.mapperx.InternalMapper";

    public static final String MAPPER_STATEMENT_SAVE_WITH_ID = "save_withId";
    public static final String MAPPER_STATEMENT_SAVE_WITHOUT_ID = "save_withoutId";
    public static final String MAPPER_STATEMENT_GET_WITH_ID = "get_withId";
    public static final String MAPPER_STATEMENT_GET_WITH_UKEY = "get_withUKey";
    public static final String MAPPER_STATEMENT_GETMULTI_WITH_ID = "getMulti_withId";
    public static final String MAPPER_STATEMENT_DELETE_WITH_ID = "delete_withId";
    public static final String MAPPER_STATEMENT_EXISTS_WITH_ID = "exists_withId";
    public static final String MAPPER_STATEMENT_UPDATE_WITH_ID = "update_withId";
    public static final String MAPPER_STATEMENT_LIST_CRITERIA = "listCriteria";
    public static final String MAPPER_STATEMENT_COUNT_CRITERIA = "countCriteria";
    public static final String MAPPER_STATEMENT_LIST_PAGE = "listPage";
    public static final String MAPPER_STATEMENT_UPDATE_DELETETAG_WITH_ID = "update_deletetag_withId";

    private static final String MAPPER_STATEMENT_TEMPLATE_LIST_CRITERIA = "listCriteriaTemplate";

    private static final String MAPPER_RESULTMAP_DEFAULT = "defaultResultMap";

    private static final IdentityHashMap<String, Object> UN_OVERRIDE_MAP = Maps.newIdentityHashMap();

    static {
        Object flag = MybatisRegister.class;
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_SAVE_WITH_ID, flag);
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_DELETE_WITH_ID, flag);
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_EXISTS_WITH_ID, flag);
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_UPDATE_WITH_ID, flag);
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_COUNT_CRITERIA, flag);
        UN_OVERRIDE_MAP.put(MAPPER_STATEMENT_UPDATE_DELETETAG_WITH_ID, flag);
    }

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private List<MapperFactoryBean> mapperFactoryBeanList = Lists.newArrayList();

    public static boolean isStatementOverride(String statement) {
        return !UN_OVERRIDE_MAP.containsKey(statement);
    }

    /**
     * 注册未完成的statement
     */
    @PostConstruct
    public void init() throws Exception {
        Configuration config = sqlSessionFactory.getConfiguration();

        //注册resultTypeHandler
        config.getTypeHandlerRegistry().register(Jsoned.class, JsonedTypeHandler.class);

        //追加默认的mapper xml
        addMapperXml("classpath:mapper/InternalMapper.xml", config);

        //追加默认的拦截器
        config.addInterceptor(new PageInterceptor());
        config.addInterceptor(new ResultTypeInterceptor());


        //以下的处理均只针对mapper类处理
        List<Class> classList = mapperFactoryBeanList.stream().map(MapperFactoryBean::getMapperInterface)
                .filter(Mapper.class::isAssignableFrom)
                .collect(Collectors.toList());

        classList.forEach(t -> {
            Class entityType = entityType(t);
            if(entityType == null || !Entity.class.isAssignableFrom(entityType) || Entity.class == entityType) {
                throw new MybatisException("相应的mapper类不能正确解析实体.类:" + t);
            }

            if(!EntityInfoHolder.registered(entityType)) {
                //noinspection unchecked
                EntityInfoHolder.register(EntityInfo.build(entityType));
            }
        });

        classList.forEach(clazz -> {
            Class entityType = entityType(clazz);
            addDefaultResultMap(clazz, entityType, config);

            addSpecialMapperStatement(clazz, config);
        });

        //通过上面的处理,之前有些未解析好的引用,现在已经可以使用了,如 _defaultResultMap,那么重新编译之前的语句
        rebuildAll(config);

        //支持partTree处理 此方法会检测已存在statement,因此在重处理之后,再进行添加处理
        classList.forEach(t -> addPartTreeStatement(t, config));

        //将statement中的resultType替换为defaultResultMap, 如果可行的话
        //这里必须复制一份,必须在循环中修改原数据结构,因为config.add会直接修改旧集合
        List<Object> allStatementList = Lists.newArrayList(config.getMappedStatements());
        allStatementList.stream()
                .filter(t -> t instanceof MappedStatement)
                .map(MappedStatement.class::cast)
                .forEach(this::replaceStatementInlineResultMap);

        //某些特殊的statement需要作调整,以支持特定的语义
        replaceSpecialStatement(config);
    }

    //---------------------------- 工具方法 start ------------------------------//

    /**
     * 将sql id和前缀拼接起来
     */
    private static String concat(String prefix, String... params) {
        StringBuilder sb = new StringBuilder(prefix);
        for(String p : params) {
            sb.append(".").append(p);
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private Class entityType(Class mapperClass) {
        return MapperUtils.getEntityType(mapperClass);
    }

    /**
     * 检查config里面之前是否已经定义了指定的statementId
     * 这里的定义,即在xml中有设定(或提前通过显示地进行设定)
     */
    private boolean hasStatement(String statementId, Configuration config) {
        //需要检测已经解析好的,以及未解析好的,因为某个statementId实际上是定义了的,但因为解析问题还没解析好
        boolean completeExists = config.hasStatement(statementId, false);
        if(completeExists) {
            return true;
        }

        //暂时未实现查询inCompleteStatement中的语义,因为其数据不太好查...

        return false;
    }

    //---------------------------- 工具方法 end ------------------------------//

    private void replaceSpecialStatement(Configuration config) {
        //将countCriteria修改为特别的处理语句
        var countCriteriaStatement = config.getMappedStatement(concat(MAPPER_NAMESPACE_INTERNAL, MAPPER_STATEMENT_COUNT_CRITERIA), false);
        countCriteriaStatement = MappedStatementUtils.copy(countCriteriaStatement.getId(), countCriteriaStatement,
                new CriteriaSqlSource(config, countCriteriaStatement.getSqlSource()), null);
        config.addMappedStatement(countCriteriaStatement);
    }

    private void replaceStatementInlineResultMap(MappedStatement statement) {
        val resultMapList = statement.getResultMaps();
        //没有resultMap,就表示可能不是select语句, 同时内联result只会有1个
        if(ObjectUtils.isEmpty(resultMapList) || resultMapList.size() != 1) {
            return;
        }

        val resultMap = resultMapList.get(0);
        //不是内联,跳过
        if(!ResultMapUtils.isInlineResultMap(resultMap)) {
            return;
        }

        val config = statement.getConfiguration();
        //检查此内联的type有没有注册默认的 defaultResultMap, 没有,表示是其它非当前处理器所控制的类型,不是一个entity(比如是一个vo类型这种)
        val defaultResultMapId = concat(resultMap.getType().getName(), MAPPER_RESULTMAP_DEFAULT);
        boolean hasResultMap = config.hasResultMap(defaultResultMapId);
        if(!hasResultMap) {
            return;
        }

        val defaultResultMap = config.getResultMap(defaultResultMapId);
        val newStatement = MappedStatementUtils.copy(statement.getId(), statement, statement.getSqlSource(),
                t -> t.resultMaps(Lists.newArrayList(defaultResultMap)));
        //强制覆盖旧的
        config.addMappedStatement(newStatement);
    }

    //---------------------------- 重新编译未处理语句 start ------------------------------//

    private void rebuildAll(Configuration config) {
        rebuildIncompleteResultMap(config);
        rebuildIncompleteStatements(config);
    }

    /**
     * 重新编译之前编译失败的所有未完成的语句
     *
     * @see XMLMapperBuilder#parsePendingResultMaps()
     */
    private void rebuildIncompleteResultMap(Configuration config) {
        val resultMapResolvers = config.getIncompleteResultMaps();
        val iter = resultMapResolvers.iterator();
        while(iter.hasNext()) {
            try{
                iter.next().resolve();
                iter.remove();
            } catch(IncompleteElementException e) {
                // ResultMap is still missing a resource...
            }
        }
    }

    /**
     * 重新编译之前编译失败的所有未完成的语句
     *
     * @see XMLMapperBuilder#parsePendingStatements()
     */
    private void rebuildIncompleteStatements(Configuration config) {
        Collection<XMLStatementBuilder> incompleteStatements = config.getIncompleteStatements();
        Iterator<XMLStatementBuilder> iter = incompleteStatements.iterator();
        while(iter.hasNext()) {
            try{
                iter.next().parseStatementNode();
                iter.remove();
            } catch(IncompleteElementException e) {
                // Statement is still missing a resource...
            }
        }
    }

    //---------------------------- 重新编译未处理语句 end ------------------------------//

    /**
     * 生成默认的resultMap
     */
    private void addDefaultResultMap(Class<?> clazz, Class entityType, Configuration configuration) {
        val entityInfo = EntityInfoHolder.get(entityType);
        List<ResultMapping> resultMappingList = entityInfo.getColumnList().stream()
                .map(t -> ResultMappingUtils.build(configuration, t)).collect(Collectors.toList());

        String[] resultMapNames = {concat(clazz.getName(), MAPPER_RESULTMAP_DEFAULT), concat(entityType.getName(), MAPPER_RESULTMAP_DEFAULT)};
        for(String resultMapName : resultMapNames) {
            //因为存在mapper继承的情况，因此可能同一个entity会添加多次,这里进行判断
            if(!configuration.hasResultMap(resultMapName)) {
                ResultMap resultMap = new ResultMap.Builder(configuration, resultMapName, entityType, resultMappingList).build();
                configuration.addResultMap(resultMap);
            }
        }
    }

    private void addMapperXml(String mapperXml, Configuration configuration) throws IOException {
        org.springframework.core.io.Resource mapperLocation = applicationContext.getResource(mapperXml);
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
                configuration, mapperXml, configuration.getSqlFragments());
        xmlMapperBuilder.parse();
    }

    private void addSpecialMapperStatement(Class<?> mapperClass, Configuration config) {
        val mapperName = mapperClass.getName();

        //需要重写的statement, 重写部分包括主键策略, 以及返回resultType调整
        List<Tuple2<String, Class>> fixTuple3List = Lists.newArrayList();
        fixTuple3List.add(new Tuple2<>(MAPPER_STATEMENT_SAVE_WITHOUT_ID, void.class));
        fixTuple3List.add(new Tuple2<>(MAPPER_STATEMENT_GET_WITH_ID, entityType(mapperClass)));
        fixTuple3List.add(new Tuple2<>(MAPPER_STATEMENT_GET_WITH_UKEY, entityType(mapperClass)));
        fixTuple3List.add(new Tuple2<>(MAPPER_STATEMENT_GETMULTI_WITH_ID, entityType(mapperClass)));
        fixTuple3List.add(new Tuple2<>(MAPPER_STATEMENT_LIST_PAGE, entityType(mapperClass)));

        fixTuple3List.forEach(t -> {
            String id = concat(mapperName, t.t1);
            val sourceStatement = config.getMappedStatement(concat(MAPPER_NAMESPACE_INTERNAL, t.t1), false);

            val overrideStatement = MappedStatementUtils.copy(id, sourceStatement, sourceStatement.getSqlSource(), b -> {
                appendStatementDefinition(b, mapperClass, t.t1);
                //修复resultType
                if(t.t2 != void.class) {
                    b.resultMaps(Lists.newArrayList(config.getResultMap(concat(mapperName, MAPPER_RESULTMAP_DEFAULT))));
                }
                b.resource(GENERATED_TAG);
            });

            config.addMappedStatement(overrideStatement);
        });

        //支持criteria处理
        val criteriaStatementTemplate = config.getMappedStatement(concat(MAPPER_NAMESPACE_INTERNAL, MAPPER_STATEMENT_TEMPLATE_LIST_CRITERIA), false);
        val criteriaStatement = MappedStatementUtils.copy(concat(mapperName, MAPPER_STATEMENT_LIST_CRITERIA), criteriaStatementTemplate,
                new CriteriaSqlSource(config, criteriaStatementTemplate.getSqlSource()),
                b -> b.resultMaps(Lists.newArrayList(config.getResultMap(concat(mapperName, MAPPER_RESULTMAP_DEFAULT)))));
        config.addMappedStatement(criteriaStatement);
    }

    private void addPartTreeStatement(Class<?> mapperClass, Configuration config) {
        val mapperName = mapperClass.getName();
        val entityClass = entityType(mapperClass);
        for(Method method : mapperClass.getMethods()) {
            //默认方法,跳过
            if(method.isDefault()) {
                continue;
            }

            val methodName = method.getName();
            val statementId = concat(mapperName, methodName);
            if(hasStatement(statementId, config)) {
                continue;
            }

            if(!PartTree.isPartTree(methodName)) {
                continue;
            }

            log.info("准备生成partTree查询.语句:{}", statementId);
            val copyStatement = config.getMappedStatement(concat(MAPPER_NAMESPACE_INTERNAL, MAPPER_STATEMENT_TEMPLATE_LIST_CRITERIA), false);
            ExceptionUtils.doActionLogE(log, () -> {
                @SuppressWarnings("unchecked")
                KClass<Entity<?>> kClass = JvmClassMappingKt.getKotlinClass(entityClass);
                val sqlSource = new PartTreeSqlSource(method, kClass, new PartTree(methodName), config);
                val partTreeStatement = MappedStatementUtils.copy(statementId, copyStatement, sqlSource,
                        b -> {
                            Class returnType = method.getReturnType();
                            ResultMap resultMap;
                            boolean isPrimitive = returnType.isPrimitive() || TypeUtils.isWrapClass(returnType);
                            if(isPrimitive && methodName.startsWith(PartTree.Companion.getCountPattern())) {
                                resultMap = new ResultMap.Builder(config, concat(mapperName, methodName, MAPPER_RESULTMAP_DEFAULT), returnType, Lists.newArrayListWithCapacity(0)).build();
                            } else {
                                resultMap = config.getResultMap(concat(mapperName, MAPPER_RESULTMAP_DEFAULT));
                            }
                            b.resultMaps(Lists.newArrayList(resultMap));
                        });

                config.addMappedStatement(partTreeStatement);
            });
        }
    }

    private void appendStatementDefinition(MappedStatement.Builder builder, Class<?> mapperClass, String statementName) {
        switch(statementName) {

            //当不使用主键列保存的时候, 则反向设置相应的主键属性
            case MAPPER_STATEMENT_SAVE_WITHOUT_ID: {
                val entityInfo = EntityInfoHolder.get(entityType(mapperClass));
                val idColumnList = entityInfo.getIdColumnList();

                //无主键列,跳过
                if(ObjectUtils.isEmpty(idColumnList)) {
                    break;
                }

                if(idColumnList.size() != 1) {
                    log.info("对象不止一个主键,不支持设置主键属性. 类:{}", entityInfo.getClazz());
                    break;
                }

                //设置相应的主键构建信息
                val idColumn = idColumnList.get(0);
                builder.keyGenerator(Jdbc3KeyGenerator.INSTANCE);
                builder.keyProperty(idColumn.getPropertyName());
                builder.keyColumn(idColumn.getColumnName());
            }
            break;
            default:
                break;
        }
    }
}
