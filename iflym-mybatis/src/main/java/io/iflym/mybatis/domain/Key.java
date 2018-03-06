package io.iflym.mybatis.domain;

import io.iflym.mybatis.domain.key.LongKey;

/**
 * 表示对一个模型的主键信息描述
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
public interface Key extends Keyed {
    /**
     * 是否已填充,即相应的主键是否已经被设置了值信息
     *
     * @return 主键已经有值了则返回true, 否则返回false. 如long型主键,如果为0,则表示没未填充
     */
    boolean fullfill();

    /**
     * 构建由单个long类型的值组合的主键,常见于单个模型通过惟一的自增id来表示主键
     *
     * @param id 单个长整形的主键值
     * @return 构建成功的主键对象
     */
    static Key of(long id) {
        return new LongKey(id);
    }
}
