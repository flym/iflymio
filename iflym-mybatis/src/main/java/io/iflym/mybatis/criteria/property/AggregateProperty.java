package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 用于描述聚合的属性,以支持类似avg,max等操作
 * created at 2018-08-15
 *
 * @author flym
 */
public class AggregateProperty extends NameProperty {
    private final String aggregateName;

    public AggregateProperty(String aggregateName, String property) {
        super(property);
        this.aggregateName = aggregateName;
    }

    @Override
    protected String toFrontSql() {
        return aggregateName + Sqls.LP + super.toFrontSql() + Sqls.RP;
    }
}
