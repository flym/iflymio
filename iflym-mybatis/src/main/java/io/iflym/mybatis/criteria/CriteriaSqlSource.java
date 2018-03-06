package io.iflym.mybatis.criteria;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * 用于支持由上下文传递参数的sql源
 * 用于支持criteria式查询, 相应的参数信息在构建时指定
 *
 * @author flym
 * Created by flym on 6/12/2016.
 */
@RequiredArgsConstructor
public class CriteriaSqlSource implements SqlSource {
    private final Configuration configuration;
    private final SqlSource sqlSource;

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new CriteriaBoundSql(configuration, sqlSource.getBoundSql(parameterObject));
    }
}