package io.iflym.mybatis.domain;

/**
 * 用于描述一个基本的实体对象
 * 此对象的设计参考jpa以及hibernate, 以提供对域模型的统一管理
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
public interface Entity<E extends Entity> extends Updatable, Lifecycle, Attribute, Copyable<E> {
    /**
     * 每个对象都应该具有主键值,此处返回相应的主键信息
     *
     * @return 具体的主键对象, 不可能返回null
     */
    Key key();
}
