package io.iflym.mybatis.mapperx.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.iflym.core.util.ListUtils;
import io.iflym.core.util.NumberUtils;
import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.CriteriaBoundSql;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.criteria.Property;
import io.iflym.mybatis.domain.*;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.domain.info.EntityInfo;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.domain.util.ColumnInfoUtils;
import io.iflym.mybatis.domain.util.UpdateUtils;
import io.iflym.mybatis.example.Example;
import io.iflym.mybatis.exception.MybatisException;
import io.iflym.mybatis.info.KeyGeneratorFactory;
import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mybatis.MybatisRegister;
import io.iflym.mybatis.util.MybatisAsserts;
import kotlin.jvm.functions.Function3;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.iflym.mybatis.mybatis.MybatisRegister.*;

/**
 * 辅助完成mapper的默认方法的实现
 *
 * @author flym
 * Created by flym on 2017/8/30.
 */
@Slf4j
public class MapperHelper {
    private static final String MAPPER_INTERNAL_NAMESPACE = "io.iflym.mybatis.mapperx.InternalMapper";

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void save(Mapper<T> mapper, T t) {
        doWithLifecycle(() -> {
            val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

            List<ColumnItem> insertList;
            String statementId;

            //支持自动生成key
            Function3<Object, ColumnInfo, T, Object> uniqueValueFun = (v, c, e) -> {
                //即不为null也不为0
                if(!ObjectUtils.isEmpty(v) && !NumberUtils.isZero(v)) {
                    return v;
                }

                if(!c.isUniqueIdColumn()) {
                    return v;
                }

                val clazz = c.getUniqueId().keyGenerator();
                if(clazz == KeyGenerator.class) {
                    return v;
                }

                val generator = KeyGeneratorFactory.INSTANCE.getKeyGenerator(clazz);
                return generator.generateKey(mapper, e, c);
            };

            val key = t.key();
            boolean keyFullFill = key.fullfill();
            if(keyFullFill) {
                statementId = MAPPER_STATEMENT_SAVE_WITH_ID;
                insertList = ListUtils.map(entityInfo.getColumnList(), c -> ColumnItem.build(c, t, uniqueValueFun));
            } else {
                statementId = MAPPER_STATEMENT_SAVE_WITHOUT_ID;
                insertList = entityInfo.getColumnList().stream()
                        .filter(c -> !c.isIdColumn())
                        .map(c -> ColumnItem.build(c, t, uniqueValueFun))
                        .collect(Collectors.toList());
            }

            val param = Maps.newHashMap();
            param.put("table", entityInfo.getTable().getTableName());
            param.put("insertList", insertList);

            val sqlSession = MapperUtils.getSqlSession(mapper);
            val statement = fullStatement(mapper, statementId);
            sqlSession.insert(statement, param);

            //如果是未填充key,则需要反写主键
            if(!keyFullFill) {
                val idColumnList = entityInfo.getIdColumnList();
                val idColumn = idColumnList.isEmpty() ? null : idColumnList.get(0);
                if(idColumn != null) {
                    val returnId = param.get(idColumn.getPropertyName());
                    ColumnInfoUtils.set(idColumn, t, returnId);
                }
            }

        }, t, Lifecycle::beforeSave, Lifecycle::afterSave);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> T get(Mapper<E> mapper, UniqueKey uniqueKey, Class<T> resultType) {
        val param = Maps.newHashMap();
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        val selectList = ListUtils.map(entityInfo.getColumnList(), ColumnItem::build);
        val ukList = toUkList(uniqueKey, entityInfo);
        val deleteTagItem = toDeleteTagItem(entityInfo.getDeleteTagColumn());

        param.put("table", entityInfo.getTable().getTableName());
        param.put("selectList", selectList);
        param.put("ukList", ukList);
        param.put("deleteTagItem", deleteTagItem);
        //支持调整结果类型
        if(resultType != null) {
            param.put("resultType", resultType);
        }

        val sqlSession = MapperUtils.getSqlSession(mapper);
        String statement = fullStatement(mapper, MAPPER_STATEMENT_GET_WITH_UKEY);

        return sqlSession.selectOne(statement, param);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> T get(Mapper<E> mapper, Key key, Class<T> resultType) {

        val param = Maps.<String, Object>newHashMap();
        //支持调整结果类型
        if(resultType != null) {
            param.put("resultType", resultType);
        }
        innerGetHandler(mapper, key, param);

        val sqlSession = MapperUtils.getSqlSession(mapper);
        String statement = fullStatement(mapper, MAPPER_STATEMENT_GET_WITH_ID);

        return sqlSession.selectOne(statement, param);
    }

    public static <E extends Entity> List<E> getMulti(Mapper<E> mapper, List<Key> keyList) {

        val param = Maps.<String, Object>newHashMap();
        innerGetMultiHandler(mapper, keyList, param);

        String statement = fullStatement(mapper, MAPPER_STATEMENT_GETMULTI_WITH_ID);
        val sqlSession = MapperUtils.getSqlSession(mapper);

        return sqlSession.selectList(statement, param);
    }

    public static <E extends Entity, T> List<T> getMulti(Mapper<E> mapper, List<Key> keyList, Class<T> resultType) {

        val param = Maps.<String, Object>newHashMap();
        assert resultType != null;
        param.put("resultType", resultType);
        innerGetMultiHandler(mapper, keyList, param);

        String statement = fullStatement(mapper, MAPPER_STATEMENT_GETMULTI_WITH_ID);
        val sqlSession = MapperUtils.getSqlSession(mapper);

        return sqlSession.selectList(statement, param);
    }

    public static <T extends Entity> boolean exists(Mapper<T> mapper, Key key) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        val idList = toIdList(key, entityInfo);
        val deleteTagItem = toDeleteTagItem(entityInfo.getDeleteTagColumn());

        val param = Maps.newHashMap();
        param.put("table", entityInfo.getTable().getTableName());
        param.put("idList", idList);
        param.put("deleteTagItem", deleteTagItem);

        val sqlSession = MapperUtils.getSqlSession(mapper);
        String statement = fullStatement(mapper, MAPPER_STATEMENT_EXISTS_WITH_ID);

        return (Boolean) sqlSession.selectOne(statement, param);
    }

    public static <T extends Entity> void delete(Mapper<T> mapper, T entity, Key key) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        val idList = toIdList(key, entityInfo);

        val deleteTagColumn = entityInfo.getDeleteTagColumn();
        //如果存在状态字段，则不进行物理删除操作，只更新状态字段（逻辑删除）
        if(!ObjectUtils.isEmpty(deleteTagColumn)) {
            updateDeleteTag(mapper, key, deleteTagColumn);
            return;
        }

        doWithLifecycle(() -> {
            val param = Maps.newHashMap();
            param.put("table", entityInfo.getTable().getTableName());
            param.put("idList", idList);

            val sqlSession = MapperUtils.getSqlSession(mapper);
            String statement = fullStatement(mapper, MAPPER_STATEMENT_DELETE_WITH_ID);

            sqlSession.delete(statement, param);
        }, entity, Entity::beforeDelete, Entity::afterDelete);
    }

    public static <E extends Entity> void update(Mapper<E> mapper, E e) {
        //如果并没有被标记，但是又调用此方法，则直接报错
        if(!UpdateUtils.marked(e)) {
            throw new MybatisException("当前对象并没有调用 upmark 方法进行标记，请检查程序");
        }

        doWithLifecycle(() -> {
            val list = e.updatedItemList();
            if(list.isEmpty()) {
                return;
            }

            val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

            //更新列表
            val columnUpdateItemList = ListUtils.map(list, t -> {
                val column = entityInfo.getColumn(t.getProperty());
                return new ColumnItem<>(column.getColumnName(), column.getColumnType(), t.getNewValue());
            });

            //主键信息
            val idItemList = toIdList(e.key(), entityInfo);

            val param = Maps.newHashMap();
            param.put("table", entityInfo.getTable().getTableName());
            param.put("updateList", columnUpdateItemList);
            param.put("idList", idItemList);

            val sqlSession = MapperUtils.getSqlSession(mapper);
            val statement = fullStatement(mapper, MAPPER_STATEMENT_UPDATE_WITH_ID);
            sqlSession.update(statement, param);
        }, e, Lifecycle::beforeUpdate, Lifecycle::afterUpdate);
    }

    private static <E extends Entity> void updateDeleteTag(Mapper<E> mapper, Key key, ColumnInfo deleteTagColumn) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        //主键信息
        val idItemList = toIdList(key, entityInfo);
        val deleteTagItem = toDeleteTagItem(deleteTagColumn);

        val param = Maps.newHashMap();
        param.put("table", entityInfo.getTable().getTableName());
        param.put("uv", deleteTagItem);
        param.put("idList", idItemList);

        val sqlSession = MapperUtils.getSqlSession(mapper);
        val statement = fullStatement(mapper, MAPPER_STATEMENT_UPDATE_DELETETAG_WITH_ID);
        sqlSession.update(statement, param);
    }

    private static List<ColumnItem> toIdList(Key key, EntityInfo<?> entityInfo) {
        val idColumnList = entityInfo.getIdColumnList();
        List<ColumnItem> idItemList = Lists.newArrayList();
        for(int i = 0; i < idColumnList.size(); i++) {
            val idColumn = idColumnList.get(i);
            idItemList.add(new ColumnItem<>(idColumn.getColumnName(), idColumn.getColumnType(), key.index(i)));
        }

        return idItemList;
    }

    private static List<ColumnItem> toUkList(UniqueKey uniqueKey, EntityInfo<?> entityInfo) {
        val group = uniqueKey.group();
        val columnList = entityInfo.getUniqueIdColumnList(group);
        MybatisAsserts.assertTrue(!ObjectUtils.isEmpty(columnList), "没有找到相应组名的惟一索引:{}", group);

        //验证个数应该一致
        int size = columnList.size();
        MybatisAsserts.assertTrue(Objects.equals(uniqueKey.length(), size), "惟一索引值的个数不正确, 期望数量:{}", size);

        List<ColumnItem> ukItemList = Lists.newArrayList();
        for(int i = 0; i < columnList.size(); i++) {
            val column = columnList.get(i);
            val item = new ColumnItem<>(column.getColumnName(), column.getColumnType(), ColumnInfoUtils.convertValue(column, uniqueKey.index(i)));
            ukItemList.add(item);
        }

        return ukItemList;
    }

    private static List<ColumnItem> toKeyedList(Keyed keyed, EntityInfo<?> entityInfo) {
        if(keyed instanceof Key) {
            return toIdList((Key) keyed, entityInfo);
        }
        if(keyed instanceof UniqueKey) {
            return toUkList((UniqueKey) keyed, entityInfo);
        }

        throw new MybatisException("暂不支持的key类型:" + keyed);
    }

    private static ColumnItem toDeleteTagItem(ColumnInfo deleteTagColumn) {
        if(deleteTagColumn == null) {
            return null;
        }
        return new ColumnItem<>(deleteTagColumn.getColumnName(), deleteTagColumn.getColumnType(), deleteTagColumn.getDeleteTagVal());
    }

    private static String fullStatement(Mapper mapper, String simpleName) {
        val currentMapper = MapperUtils.getMapperClass(mapper);
        val prefix = MybatisRegister.isStatementOverride(simpleName) ? currentMapper.getName() : MAPPER_INTERNAL_NAMESPACE;
        return prefix + "." + simpleName;
    }

    public static <E extends Entity> List<E> listCriteria(Mapper<E> mapper, Criteria<E> criteria) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));
        val deleteTagItem = entityInfo.getDeleteTagColumn();
        if(deleteTagItem != null) {
            criteria.where(Criterion.notEq(deleteTagItem.getPropertyName(), deleteTagItem.getDeleteTagVal()));
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("sql", criteria.toSql());

        map.put(CriteriaBoundSql.PARAM_VALUE_KEY_NAME, criteria.fetchParams());

        val sqlSession = MapperUtils.getSqlSession(mapper);
        val statement = fullStatement(mapper, MAPPER_STATEMENT_LIST_CRITERIA);

        return sqlSession.selectList(statement, map);
    }


    public static <E extends Entity> int countCriteria(Mapper<E> mapper, Criteria<E> criteria) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));
        val deleteTagColumn = entityInfo.getDeleteTagColumn();
        if(deleteTagColumn != null) {
            criteria.where(Criterion.notEq(deleteTagColumn.getPropertyName(), deleteTagColumn.getDeleteTagVal()));
        }
        Criteria countCriteria = criteria.clone();
        //清除对count查询不影响的语句
        countCriteria.clearSelect().select(Property.countOne().setAlias("value"))
                .clearOrder()
                //这句因为可能原来已经有limit了,这里的count查询不需要limit
                .clearLimit();

        Map<String, Object> map = Maps.newHashMap();
        map.put("sql", countCriteria.toSql());
        map.put(CriteriaBoundSql.PARAM_VALUE_KEY_NAME, countCriteria.fetchParams());

        val sqlSession = MapperUtils.getSqlSession(mapper);
        val statement = fullStatement(mapper, MAPPER_STATEMENT_COUNT_CRITERIA);

        return sqlSession.selectOne(statement, map);
    }

    public static <E extends Entity> List<E> listPage(Mapper<E> mapper, Page page) {

        val param = Maps.<String, Object>newHashMap();
        innerListPageHandler(mapper, param);

        val sqlSession = MapperUtils.getSqlSession(mapper);
        String statement = fullStatement(mapper, MAPPER_STATEMENT_LIST_PAGE);

        return sqlSession.selectList(statement, param, page);
    }

    public static <E extends Entity, T> List<T> listPage(Mapper<E> mapper, Page page, Class<T> resultType) {

        val param = Maps.<String, Object>newHashMap();
        assert resultType != null;
        param.put("resultType", resultType);
        innerListPageHandler(mapper, param);

        val sqlSession = MapperUtils.getSqlSession(mapper);
        String statement = fullStatement(mapper, MAPPER_STATEMENT_LIST_PAGE);

        return sqlSession.selectList(statement, param, page);
    }

    @RequiredArgsConstructor
    @Getter
    private static class ColumnItem<V> {
        /**
         * 列名
         */
        private final String column;
        /**
         * 类型
         */
        private final JdbcType jdbcType;
        /**
         * 值
         */
        private final V value;

        private static <V> ColumnItem<V> build(ColumnInfo columnInfo) {
            return build(columnInfo, null);
        }

        /**
         * 使用指定的列对象以及具体的实例对象构建数据值对象
         */
        @SuppressWarnings("unchecked")
        private static <V> ColumnItem<V> build(ColumnInfo columnInfo, Entity entity) {
            return build(columnInfo, entity, null);
        }

        @SuppressWarnings("unchecked")
        private static <V> ColumnItem<V> build(ColumnInfo columnInfo, Entity entity, Function3 valueChanger) {
            V value = null;
            if(entity != null) {
                value = ColumnInfoUtils.get(columnInfo, entity);
                if(valueChanger != null) {
                    value = (V) valueChanger.invoke(value, columnInfo, entity);
                }
            }

            return new ColumnItem<>(columnInfo.getColumnName(), columnInfo.getColumnType(), value);
        }

    }

    private static <E extends Entity> void innerGetHandler(Mapper<E> mapper, Key key, Map<String, Object> paramMap) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        val selectList = ListUtils.map(entityInfo.getColumnList(), ColumnItem::build);
        val idList = toIdList(key, entityInfo);
        val deleteTagItem = toDeleteTagItem(entityInfo.getDeleteTagColumn());

        paramMap.put("table", entityInfo.getTable().getTableName());
        paramMap.put("selectList", selectList);
        paramMap.put("idList", idList);
        paramMap.put("deleteTagItem", deleteTagItem);
    }

    private static <E extends Entity> void innerGetMultiHandler(Mapper<E> mapper, List<Key> keyList, Map<String, Object> paramMap) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        //选择列
        val selectList = ListUtils.map(entityInfo.getColumnList(), ColumnItem::build);

        //查询主键条件
        val idColumnList = entityInfo.getIdColumnList();
        val idItemListList = Lists.newArrayList();
        for(Key key : keyList) {
            val idItemList = toIdList(key, entityInfo);
            idItemListList.add(idItemList);
        }
        val deleteTagItem = toDeleteTagItem(entityInfo.getDeleteTagColumn());

        paramMap.put("table", entityInfo.getTable().getTableName());
        paramMap.put("selectList", selectList);
        paramMap.put("idListList", idItemListList);
        paramMap.put("deleteTagItem", deleteTagItem);
    }

    private static <E extends Entity> void innerListPageHandler(Mapper<E> mapper, Map<String, Object> paramMap) {
        val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

        val selectList = ListUtils.map(entityInfo.getColumnList(), ColumnItem::build);

        val deleteTagItem = toDeleteTagItem(entityInfo.getDeleteTagColumn());

        paramMap.put("table", entityInfo.getTable().getTableName());
        paramMap.put("selectList", selectList);
        paramMap.put("deleteTagItem", deleteTagItem);
    }

    /** 支持生命周期式调用 */
    private static <E extends Entity> void doWithLifecycle(Runnable function0, E e, Consumer<E> beforeFun, Consumer<E> afterFun) {
        if(e != null) {
            beforeFun.accept(e);
        }

        function0.run();

        if(e != null) {
            afterFun.accept(e);
        }
    }

    public static <E extends Entity<E>> void updateByExample(Mapper<E> mapper, Example<E> example) {
        doWithLifecycle(()->{
            val entityInfo = EntityInfoHolder.get(MapperUtils.getEntityType(mapper));

            val columnItemMap = example.getColumnItemMap();
            val columnUpdateItemList = columnItemMap.entrySet().stream().
                    map(t -> new ColumnItem<>(t.getKey().getColumnName(), t.getKey().getColumnType(), t.getValue()))
                    .collect(Collectors.toList());

            //如果没更新的数据,则直接略过
            if(ObjectUtils.isEmpty(columnUpdateItemList)) {
                log.debug("没有要更新的数据,{}", example);
                return;
            }

            //惟一主键信息
            Keyed key = example.fetchKeyed();
            val idItemList = toKeyedList(key, entityInfo);

            val param = Maps.newHashMap();
            param.put("table", entityInfo.getTable().getTableName());
            param.put("updateList", columnUpdateItemList);
            param.put("idList", idItemList);

            val sqlSession = MapperUtils.getSqlSession(mapper);
            val statement = fullStatement(mapper, MAPPER_STATEMENT_UPDATE_WITH_ID);
            sqlSession.update(statement, param);
        }, example.getEntity(), Lifecycle::beforeUpdate, Lifecycle::afterUpdate);
    }
}
