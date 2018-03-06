package io.iflym.mybatis.criteria.criterion;

import io.iflym.core.tuple.Tuple2;
import io.iflym.core.util.ExceptionUtils;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.criteria.util.PropertyUtils;

/**
 * 简化相应的实现
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public abstract class AbstractCriterion implements Criterion {
    /** 原始所对应的查询源 */
    protected Criteria criteria;

    @Override
    public void injectCriteria(Criteria criteria) {
        //仅绑定一次,避免重新绑定以及替换源
        if(this.criteria == null) {
            this.criteria = criteria;
        }
    }

    @Override
    public AbstractCriterion clone() {
        return ExceptionUtils.doFunRethrowE(() -> (AbstractCriterion) super.clone());
    }

    /** 将相应的查询属性拆分为查询源和查询属性 */
    protected static Tuple2<String, String> splitProperty(String property) {
        return PropertyUtils.splitProperty(property);
    }
}
