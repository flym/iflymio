package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述大于等于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class GeCriterion<T> extends SimpleExpression<T> {
    public GeCriterion(String property, T value) {
        super(property, value, Sqls.GE);
    }
}
