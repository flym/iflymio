package io.iflym.mybatis.domain.key;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 任意个数的惟一索引值
 * <p>
 * Created by flym on 2017/12/25.
 *
 * @author flym
 */
@RequiredArgsConstructor
@ToString(of = {"group", "values"})
public class AnyUniqueKey extends AbstractUniqueKey {
    /** 组名 */
    private final String group;

    /** 相应的值 */
    private final Object[] values;

    @Override
    public String group() {
        return group;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <V> V doIndex(int i) {
        return (V) values[i];
    }

    @Override
    public int length() {
        return values.length;
    }
}
