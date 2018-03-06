package io.iflym.mybatis.mapperx.util;

import com.google.common.base.Joiner;
import io.iflym.core.util.ObjectUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.function.Consumer;

/**
 * 工具类,用于对statement作进一步处理
 *
 * @author flym
 * Created by flym on 2017/9/11.
 */
public class MappedStatementUtils {
    public static MappedStatement copy(String id, MappedStatement source, SqlSource sqlSource, Consumer<MappedStatement.Builder> action) {
        final Configuration configuration = source.getConfiguration();

        MappedStatement.Builder builder = new MappedStatement.Builder(configuration, id, sqlSource, source.getSqlCommandType());

        builder.resource(source.getResource());
        builder.fetchSize(source.getFetchSize());
        builder.statementType(source.getStatementType());
        builder.keyGenerator(source.getKeyGenerator());
        if(!ObjectUtils.isEmpty(source.getKeyProperties())) {
            builder.keyProperty(Joiner.on(',').join(source.getKeyProperties()));
        }
        builder.timeout(source.getTimeout());
        builder.parameterMap(source.getParameterMap());
        builder.resultMaps(source.getResultMaps());
        builder.resultSetType(source.getResultSetType());
        builder.cache(source.getCache());
        builder.flushCacheRequired(source.isFlushCacheRequired());
        builder.useCache(source.isUseCache());

        if(action != null) {
            action.accept(builder);
        }

        return builder.build();
    }
}
