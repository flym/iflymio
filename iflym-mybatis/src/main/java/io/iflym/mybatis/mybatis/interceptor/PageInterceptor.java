package io.iflym.mybatis.mybatis.interceptor;

import com.google.common.collect.Lists;
import io.iflym.mybatis.domain.Page;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于支持带page参数的分页处理
 *
 * @author flym
 * Created by flym on 2017/9/4.
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Slf4j
public class PageInterceptor implements Interceptor {

    /** 不分页,或针对已经分页查询的数据 */
    private static final RowBounds NO_ROW_BOUNDS = RowBounds.DEFAULT;

    private static final String START_INDEX = "startIndex_";
    private static final String END_INDEX = "endIndex_";
    private static final String PAGE_RESULT_MAP = "_page";

    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUND_INDEX = 2;
    private static final int RESULT_HANDLER_INDEX = 3;

    /** 对分页resultMap的引用 */
    private transient ResultMap pageResultMap;

    private ResultMap getPageResultMap(Configuration config) {
        if(pageResultMap == null) {
            pageResultMap = config.getResultMap(PAGE_RESULT_MAP);
        }

        return pageResultMap;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept((Executor) invocation.getTarget(), invocation.getArgs());
        return invocation.proceed();
    }

    private void processIntercept(Executor executor, final Object[] args) throws SQLException {
        final RowBounds rowBounds = (RowBounds) args[ROWBOUND_INDEX];
        //只有提供分页信息时,才处理
        if(rowBounds == null) {
            return;
        }
        if(rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT) {
            return;
        }
        boolean isPage = rowBounds instanceof Page;

        MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object parameter = args[PARAMETER_INDEX];
        val oldBoundSql = ms.getBoundSql(parameter);
        val oldSql = oldBoundSql.getSql().trim();

        //处理总记录数
        if(isPage) {
            Page page = (Page) rowBounds;
            //只有需要查询总数的时候才查询,避免重复查询
            if(page.getCount() == 0) {
                int totalCount;

                String countSql = getCountSql(oldSql);
                BoundSql countBoundSql = buildNewBoundSql(ms, oldBoundSql, countSql, NO_ROW_BOUNDS);
                //这时将要resultType设置为long类型,以取得总记录数信息
                ResultMap pageResultMap = getPageResultMap(ms.getConfiguration());

                MappedStatement countMappedStatement = buildNewStatement(ms.getId(), ms, new BoundSqlSqlSource(countBoundSql), Collections.singletonList(pageResultMap));
                List<Integer> list = executor.query(countMappedStatement, parameter, NO_ROW_BOUNDS, Executor.NO_RESULT_HANDLER);
                totalCount = list.get(0);
                page.setCount(totalCount);
            }
        }

        //避免结果集再处理一次
        args[ROWBOUND_INDEX] = NO_ROW_BOUNDS;

        //处理记录信息
        String pageSql = getPageSql(oldSql);
        BoundSql pageBoundSql = buildNewBoundSql(ms, oldBoundSql, pageSql, rowBounds);
        MappedStatement newMs = buildNewStatement(ms.getId(), ms, new BoundSqlSqlSource(pageBoundSql), ms.getResultMaps());
        args[MAPPED_STATEMENT_INDEX] = newMs;
    }

    private BoundSql buildNewBoundSql(MappedStatement ms, BoundSql boundSql, String sql, RowBounds rowBounds) {
        List<ParameterMapping> parameterMapping = Lists.newArrayList(boundSql.getParameterMappings());

        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMapping, boundSql.getParameterObject());
        for(ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if(boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        //处理额外的分页参数
        if(rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
            parameterMapping = newBoundSql.getParameterMappings();
            parameterMapping.add(new ParameterMapping.Builder(ms.getConfiguration(), START_INDEX, int.class).build());
            parameterMapping.add(new ParameterMapping.Builder(ms.getConfiguration(), END_INDEX, int.class).build());

            newBoundSql.setAdditionalParameter(START_INDEX, rowBounds.getOffset());
            newBoundSql.setAdditionalParameter(END_INDEX, rowBounds.getLimit());
        }
        return newBoundSql;
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //nothing to do
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private static final Pattern PATTERN_SELECT = Pattern.compile("(?<=select)[\\s\\S]*?(?=from)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_ORDER_BY = Pattern.compile("^[\\s\\S]+\\s+(order\\s+by\\s+\\w+(?:\\.\\w+)?+(?:\\s+(?:asc|desc))?\\s*(?:,\\s*\\w+(?:\\.\\w+)?(?:\\s+(?:asc|desc))?\\s*)*)\\s*$", Pattern.CASE_INSENSITIVE);

    public String getCountSql(String sql) {
        //将 select xxx from 中间的数据替换为1 这里面的xxx可能为回车符
        Matcher matcher = PATTERN_SELECT.matcher(sql);
        if(matcher.find()) {
            sql = matcher.replaceFirst(" 1 ");
        }

        //去末尾order by(如果有)
        matcher = PATTERN_ORDER_BY.matcher(sql);
        if(matcher.find()) {
            //匹配里面的order by 内容
            String groupS = matcher.group(1);
            //这里直接substring因为上面的groups即最后一段内容
            sql = sql.substring(0, sql.length() - groupS.length());
        }

        return "select count(1) from (" + sql + ") cnt_";
    }

    public String getPageSql(String sql) {
        return sql + " limit ?,?";
    }

    /** 对原来的信息进行复制,并采用新的id,sqlSource和新的结果映射 */
    public static MappedStatement buildNewStatement(String id, MappedStatement ms, SqlSource newSqlSource, List<ResultMap> resultMapList) {
        final Configuration configuration = ms.getConfiguration();

        MappedStatement.Builder builder = new MappedStatement.Builder(configuration, id, newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        //todo 这里实际上没有keyProperties信息
        builder.keyProperty("");
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(resultMapList);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
