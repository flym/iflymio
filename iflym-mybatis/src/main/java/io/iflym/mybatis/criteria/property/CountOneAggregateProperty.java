package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.mapperx.Sqls;

import static io.iflym.mybatis.mapperx.Sqls.CONST_1;
import static io.iflym.mybatis.mapperx.Sqls.RP;

/**
 * 描述统计查询中的 count(1)
 * created at 2018-08-15
 *
 * @author flym
 */
public class CountOneAggregateProperty extends SqlProperty {
    public CountOneAggregateProperty() {
        super(Sqls.COUNT + Sqls.LP + CONST_1 + RP);
    }
}
