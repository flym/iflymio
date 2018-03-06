package io.iflym.mybatis.domain.key;

import io.iflym.mybatis.domain.Keyed;

/**
 * 实现部分索引级功能,并提供安全性校验功能
 * Created by flym on 2017/8/30.
 *
 * @author flym
 */
public abstract class AbstractKeyed implements Keyed {
    @Override
    public <V> V index(int i) {
        if(i >= length()) {
            throw new IndexOutOfBoundsException("主键值超限:" + i);
        }

        return doIndex(i);
    }

    /**
     * 进行具体的获取相应位置值的处理
     *
     * @param i 相应的索引位
     * @return 相应位置上的值
     */
    protected abstract <V> V doIndex(int i);
}
