package io.iflym.mybatis.domain;

/**
 * 描述具有生命周期的对象信息, 可以在特定的时间点进行调用和处理
 * Created by flym on 2017/12/11.
 *
 * @author flym
 */
public interface Lifecycle {
    /** 在保存之前触发,以允许业务进行特定操作 */
    default void beforeSave() {
    }

    /** 在保存之后触发,以允许业务进行特定操作 */
    default void afterSave() {
    }

    /** 在更新之前触发,以允许业务进行特定操作 */
    default void beforeUpdate() {
    }

    /** 在更新之后触发,以允许业务进行特定操作 */
    default void afterUpdate() {
    }

    /** 在删除之前触发,以允许业务进行特定操作 */
    default void beforeDelete() {
    }

    /** 在删除之后触发,以允许业务进行特定操作 */
    default void afterDelete() {
    }
}
