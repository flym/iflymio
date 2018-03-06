package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述属性相等的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class EqPropertyCriterion extends PropertyExpression {
    public EqPropertyCriterion(String propertyA, String propertyB) {
        super(propertyA, propertyB, Sqls.EQ);
    }
}
