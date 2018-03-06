package io.iflym.core.util.methodutils;

import java.util.HashMap;

/**
 * 这里会构建一个随机字符串,每次调用put时即会修改此值
 * <p>
 * Created by flym on 2017/11/1.
 */
public class FirstMap extends HashMap {
    public String randomStr;

    @Override
    @SuppressWarnings("unchecked")
    public Object put(Object key, Object value) {
        randomStr = String.valueOf(Math.random());
        return super.put(key, value);
    }
}
