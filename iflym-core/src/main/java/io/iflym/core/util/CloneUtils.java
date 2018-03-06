package io.iflym.core.util;

import java.lang.reflect.Method;

/**
 * clone工具类,支持对对象进行clone
 * Created by flym on 2017/11/8.
 *
 * @author flym
 */
public class CloneUtils {
    private static Method cloneMethod;

    static {
        ExceptionUtils.doActionRethrowE(() -> {
            cloneMethod = Object.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
        });
    }

    /** 对指定的对象进行clone处理，如果不能clone，则throw相应的异常 */
    @SuppressWarnings("unchecked")
    public static <T extends Cloneable> T clone(T t) {
        return (T) ExceptionUtils.doFunRethrowE(() -> cloneMethod.invoke(t));
    }
}
