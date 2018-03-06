package io.iflym.mybatis.domain;

import io.iflym.mybatis.domain.key.AnyUniqueKey;
import io.iflym.mybatis.domain.key.StringUniqueKey;

/**
 * 对一个惟一索引的描述信息
 * Created by flym on 2017/12/25.
 *
 * @author flym
 */
public interface UniqueKey extends Keyed {
    String GROUP_DEFAULT = "default";

    /**
     * 返回此惟一索引的组
     *
     * @return 组名
     */
    String group();

    /**
     * 构建一个默认的字符串的惟一索引
     *
     * @param value 字符串
     * @return 惟一索引对象
     */
    static UniqueKey ofDefaultString(String value) {
        return new StringUniqueKey(UniqueKey.GROUP_DEFAULT, value);
    }

    /**
     * 构建一个默认组的惟一索引,其值的个数与组名相对应
     *
     * @param values 与组名相对应的值,其顺序也应该与定义时的顺序相一致
     * @return 惟一索引对象
     */
    static UniqueKey ofDefault(Object... values) {
        return new AnyUniqueKey(GROUP_DEFAULT, values);
    }

    /**
     * 构建一个指定组名,指定任意个值的惟一索引对象
     *
     * @param group  组名
     * @param values 值列表
     * @return 索引对象
     */
    static UniqueKey of(String group, Object... values) {
        return new AnyUniqueKey(group, values);
    }
}
