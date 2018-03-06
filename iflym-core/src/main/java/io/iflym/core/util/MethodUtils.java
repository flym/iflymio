package io.iflym.core.util;

import com.google.common.collect.Maps;
import io.iflym.core.tuple.Tuple1Holder;
import lombok.experimental.var;
import lombok.val;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 用于简化对于方法的访问和调用
 *
 * @author flym
 * Created by flym on 2017/10/25.
 */
public class MethodUtils {
    private static final Map<Method, MethodHandle> SPECIAL_METHOD_HANDLE_MAP = Maps.newHashMap();

    private static final Constructor<MethodHandles.Lookup> LOOKUP_PRIVATE;

    static {
        Tuple1Holder<Constructor<MethodHandles.Lookup>> constructorHolder = new Tuple1Holder<>();
        ExceptionUtils.doActionRethrowE(() -> {
            Constructor constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);
            //noinspection unchecked
            constructorHolder.t1 = constructor;
        });

        LOOKUP_PRIVATE = constructorHolder.t1;
    }

    /** 获取一个方法的简化表示，仅输出类名+方法名 */
    public static String toSimpleString(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    /**
     * 采用 invokeSpecial调用一个方法,此方法调用不会执行override处理
     * 应用场景,比如子类覆盖了此方法,但在其它地方需要使用子类对象,直接调用父类的这个方法,绕过子类的场景.
     * 如代理接口default方法,或者是 super.super.call()的类似调用
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeSpecial(Method method, Object instance, Object... params) {
        val declaringClass = method.getDeclaringClass();
        return (T) ExceptionUtils.doFunRethrowE(() -> {
            var handle = SPECIAL_METHOD_HANDLE_MAP.get(method);
            if(handle == null) {
                handle = LOOKUP_PRIVATE.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                        .unreflectSpecial(method, declaringClass);
                SPECIAL_METHOD_HANDLE_MAP.put(method, handle);
            }

            return handle.bindTo(instance).invokeWithArguments(params);
        });
    }
}
