package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述属性不相等的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class NotEqPropertyCriterion extends PropertyExpression {
    public NotEqPropertyCriterion(String propertyA, String propertyB) {
        super(propertyA, propertyB, Sqls.NEQ);
    }
}
