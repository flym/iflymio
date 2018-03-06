package io.iflym.mybatis.criteria.criterion;

import static io.iflym.mybatis.mapperx.Sqls.LE;

/**
 * 描述小于等于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class LeCriterion<T> extends SimpleExpression<T> {
    public LeCriterion(String property, T value) {
        super(property, value, LE);
    }
}
