package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述大于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class GtCriterion<T> extends SimpleExpression<T> {
    public GtCriterion(String property, T value) {
        super(property, value, Sqls.GT);
    }
}
