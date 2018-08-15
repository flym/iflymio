package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述统计查询中的 count(1)
 * created at 2018-08-15
 *
 * @author flym
 */
public class CountDistinctAggregateProperty extends AggregateProperty {
    public CountDistinctAggregateProperty(String property) {
        super(Sqls.COUNT, property);
    }

    @Override
    protected String toFrontSql() {
        return Sqls.COUNT + Sqls.LP + Sqls.DISTINCT + super.toFrontSql() + Sqls.RP;
    }
}
