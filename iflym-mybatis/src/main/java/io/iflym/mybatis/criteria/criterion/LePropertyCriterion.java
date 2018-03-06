package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述属性小于等于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class LePropertyCriterion extends PropertyExpression {
    public LePropertyCriterion(String propertyA, String propertyB) {
        super(propertyA, propertyB, Sqls.LE);
    }
}
