package io.iflym.mybatis.mybatis.interceptor;

import com.google.common.collect.Lists;
import io.iflym.mybatis.mapperx.util.MappedStatementUtils;
import lombok.val;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

/**
 * 动态设置 MyBatis 返回值类型
 * 通过@Param("resultType")指定返回类型
 * 示例：
 * 声明：Object selectByUid(@Param("uid")Long uid, @Param("resultType") Class resultType);
 * 调用：User user = mapper.selectById(1L, User.class)
 *
 * @author luyi
 * @date 2017/11/24
 */
@Intercepts(@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class ResultTypeInterceptor implements Interceptor {

    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<>(0);
    public static final String DEFAULT_KEY = "resultType";
    private String resultType = DEFAULT_KEY;

    /**
     * invocation.getArgs[]:
     * args[0]: mappedStatement
     * args[1]: parameterObject
     * args[2]: rowBounds
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        Class resultType = getResultType(parameterObject);
        if (resultType == null) {
            return invocation.proceed();
        }
        args[0] = buildNewMappedStatement(ms, resultType);
        return invocation.proceed();
    }

    /**
     * 根据现有的 ms 创建一个新的MappedStatement，使用新的返回值类型
     */
    private MappedStatement buildNewMappedStatement(MappedStatement ms, Class resultType) {
        final Configuration config = ms.getConfiguration();
        String msId = ms.getId() + "_" + resultType.getSimpleName();
        try {
            Optional<MappedStatement> optionalMs = Optional.ofNullable(config.getMappedStatement(msId));
            if (optionalMs.isPresent()) {
                return optionalMs.get();
            }
        } catch (IllegalArgumentException e) {
            // ignore, 首次从缓存中获取会抛错
            // ibatis StrictMap在get不到ms时会抛错，忽略该错误， 待新创建ms加入configuration后即可获取
        }
        ResultMap resultMap = new ResultMap.Builder(config, msId, resultType, EMPTY_RESULTMAPPING).build();
        val newStatement = MappedStatementUtils.copy(msId, ms, ms.getSqlSource(),
                t -> t.resultMaps(Lists.newArrayList(resultMap)));
        config.addMappedStatement(newStatement);
        return newStatement;
    }

    /**
     * 获取设置的返回值类型, 只支持根据map参数获取
     */
    private Class getResultType(Object parameterObject) {
        if (parameterObject == null) {
            return null;
        } else if (parameterObject instanceof Map) {
            if (((Map) (parameterObject)).containsKey(resultType)) {
                Object result = ((Map) (parameterObject)).get(resultType);
                return objectToClass(result);
            }
        }
        return null;
    }

    /**
     * 将结果转换为Class
     */
    private Class objectToClass(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Class) {
            return (Class) object;
        } else {
            throw new RuntimeException("方法参数类型错误，" + resultType + " 对应的参数类型只能为 Class 类型");
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String resultType = properties.getProperty("resultType");
        if (resultType != null && resultType.length() > 0) {
            this.resultType = resultType;
        }
    }
}
