/* Created by flym at 10/14/16 */
package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.criteria.util.ValueUtils;

/**
 * 描述一个特定的值的属性，即直接查询此值信息
 * <p>
 * 注: 此实现未处理潜在的sql注入,请使用者自行对潜在的注入情况进行处理
 *
 * @author flym
 */
public class ValueProperty<T> extends AbstractAliasProperty {
    /** 相应的常量值 */
    private final T value;

    public ValueProperty(T value) {
        this.value = value;
    }

    @Override
    protected String toFrontSql() {
        return ValueUtils.value2str(value);
    }
}
