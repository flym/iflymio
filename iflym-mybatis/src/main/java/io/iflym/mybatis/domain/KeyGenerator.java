package io.iflym.mybatis.domain;

import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.mapperx.Mapper;

/**
 * @author luyi
 * @date 2017/12/11
 */
public interface KeyGenerator<T extends Entity<T>, V> {

    /** 必要的初始化方法 */
    default void init() {
    }

    /**
     * 生成UKey方法
     *
     * @param mapper     mapper<Entity>
     * @param entity     entity
     * @param columnInfo 当前所处理的列属性
     * @return Object
     */
    V generateKey(Mapper<T> mapper, T entity, ColumnInfo columnInfo);
}
