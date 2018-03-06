package io.iflym.mybatis.criteria.criterion;

import static io.iflym.mybatis.mapperx.Sqls.LT;

/**
 * 描述属性小于的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class LtPropertyCriterion extends PropertyExpression {
    public LtPropertyCriterion(String propertyA, String propertyB) {
        super(propertyA, propertyB, LT);
    }
}
