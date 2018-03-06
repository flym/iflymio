package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述不相等的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class NotEqCriterion<T> extends SimpleExpression<T> {
    public NotEqCriterion(String property, T value) {
        super(property, value, Sqls.NEQ);
    }
}
