package io.iflym.mybatis.domain.key;

import lombok.AllArgsConstructor;

/**
 * 用单个long值表示的主键
 * Created by flym on 2017/8/29.
 *
 * @author flym
 */
@AllArgsConstructor
public class LongKey extends AbstractKey {
    private long id;

    @Override
    public boolean fullfill() {
        return id != 0;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <V> V doIndex(int i) {
        return (V) Long.valueOf(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
