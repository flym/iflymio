package io.iflym.mybatis.domain.key;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 实现使用字符串实现的惟一索引功能
 * Created by flym on 2017/12/25.
 *
 * @author flym
 */
@RequiredArgsConstructor
@ToString(of = {"group", "value"})
public class StringUniqueKey extends AbstractUniqueKey {
    /** 相应的组名 */
    private final String group;
    /** 相应的值 */
    private final String value;

    @Override
    public String group() {
        return group;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <V> V doIndex(int i) {
        return (V) value;
    }

    @Override
    public int length() {
        return 1;
    }
}
