package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.criteria.Property;

/**
 * 描述具有单个查询属性的查询条件
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public interface SingleProperty extends Property {
    /**
     * 设置相应的别名信息
     *
     * @param alias 具体的别名
     * @return 返回当前对象, this
     */
    SingleProperty setAlias(String alias);
}
