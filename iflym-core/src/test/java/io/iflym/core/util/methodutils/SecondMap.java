package io.iflym.core.util.methodutils;

import io.iflym.core.util.MethodUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 这里构建一个first的子类,用于重写相应的put
 * Created by flym on 2017/11/1.
 */
public class SecondMap extends FirstMap {
    private static final Method putMethod = ReflectionUtils.findMethod(HashMap.class, "put", Object.class, Object.class);

    @Override
    public Object put(Object key, Object value) {
        return MethodUtils.invokeSpecial(putMethod, this, key, value);
    }
}
