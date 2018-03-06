package io.iflym.mybatis.domain;

import io.iflym.mybatis.domain.util.UpdateUtils;

import java.util.List;

/**
 * 描述可以被修改的对象
 * 即对象是可以被修改的, 在进行修改的时候需要进行相应的状态记录,以反向具体对象的变化情况
 *
 * @author flym
 * Created by flym on 2017/9/1.
 */
public interface Updatable {
    /** 更新标记 */
    default void upmark() {
        UpdateUtils.markUpdate(this);
    }

    /** 取消标识,清除相应的状态 */
    default void unUpmark() {
        UpdateUtils.unmarkUpdate(this);
    }

    /**
     * 返回已经被修改的条目数据
     *
     * @return 相应的修改数据, 如果没有修改无则返回emptyList
     */
    @SuppressWarnings("unchecked")
    default List<UpdateItem<?, ?>> updatedItemList() {
        return (List) UpdateUtils.getUpdateItemList(this);
    }
}
