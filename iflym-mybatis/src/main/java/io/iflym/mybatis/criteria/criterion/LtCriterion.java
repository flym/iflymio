package io.iflym.mybatis.criteria.criterion;

import static io.iflym.mybatis.mapperx.Sqls.LT;

/**
 * 描述小于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class LtCriterion<T> extends SimpleExpression<T> {
    public LtCriterion(String property, T value) {
        super(property, value, LT);
    }
}
