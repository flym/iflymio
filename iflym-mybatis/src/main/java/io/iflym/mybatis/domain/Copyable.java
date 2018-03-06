package io.iflym.mybatis.domain;

import io.iflym.core.util.CloneUtils;

/**
 * 描述可以被copy的对象
 * Created by flym on 2017/11/8.
 * 通过一个额外的clone方法, 达到数据复制的目的
 * 不使用一般的clone方法, 以避免在理解代码时不清楚相应的逻辑
 *
 * @author flym
 */
public interface Copyable<V extends Copyable> extends Cloneable {
    /**
     * 将自己作一份copy操作,以返回一个新的副本数据
     *
     * @return 当前对象的一个副本, 为浅copy
     */
    @SuppressWarnings("unchecked")
    default V copy() {
        return (V) CloneUtils.clone(this);
    }
}
