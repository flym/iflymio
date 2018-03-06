package io.iflym.mybatis.criteria;

/**
 * 用于描述针对属性的条件处理
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public interface PropertyCriterion extends Criterion {

    /**
     * 绑定第二个查询源,即在整个基于属性查询的条件中的右边的查询源
     *
     * @param otherCriteria 第2个查询源对象
     */
    void injectOtherCriteria(Criteria otherCriteria);

    /**
     * 复制一个新的对象以方便修改
     *
     * @return 新的对象, 状态数据与原对象相同
     */
    @Override
    PropertyCriterion clone();
}
