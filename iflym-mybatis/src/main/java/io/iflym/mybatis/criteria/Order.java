package io.iflym.mybatis.criteria;

import io.iflym.mybatis.criteria.order.NameOrder;

/**
 * 描述一个具体的排序条件
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public interface Order extends Cloneable {
    /**
     * 注入相应的查询源
     *
     * @param criteria 相应的查询源对象
     */
    void injectCriteria(Criteria criteria);

    /** 必要的初始化 */
    default void init() {
        //nothing to do
    }

    /**
     * 相应的sql形式
     *
     * @return 生成的order by sql语句
     */
    String toSql();

    /**
     * 获取相应的属性信息
     *
     * @return 此order by 所对应的属性
     */
    String getProperty();

    /**
     * 是否升序
     *
     * @return 如果是升序, 则返回true
     */
    boolean isAsc();

    /**
     * 克隆出一个新的对象
     *
     * @return 与原对象不一样的新对象, 状态数据复制一份
     */
    Order clone();

    /**
     * 创建出升序排序
     *
     * @param property 升序的实体属性
     * @return 生成的升序排序对象
     */
    static Order asc(String property) {
        return new NameOrder(property, true);
    }

    /**
     * 创建出降序排序
     *
     * @param property 降序的实体属性
     * @return 生成的降序排序对象
     */
    static Order desc(String property) {
        return new NameOrder(property, false);
    }
}
