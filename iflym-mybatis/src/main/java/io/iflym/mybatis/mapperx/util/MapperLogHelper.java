package io.iflym.mybatis.mapperx.util;

import io.iflym.mybatis.mapperx.Mapper;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 允许在执行过程中外部一些程序可以拿到实际
 * created at 2018-10-08
 *
 * @author flym
 */
public class MapperLogHelper {
    private static ThreadLocal<String> loggerNameThreadLocal = new ThreadLocal<>();

    /** 当前绑定的mapper日志 */
    public static Optional<String> bindedMapperLog() {
        return Optional.ofNullable(loggerNameThreadLocal.get());
    }

    public static <V> V callWithinMapperLog(Mapper mapper, String methodName, Supplier<V> run) {
        try{
            loggerNameThreadLocal.set(MapperUtils.getMapperClass(mapper).getName() + "." + methodName);
            return run.get();
        } finally {
            loggerNameThreadLocal.remove();
        }
    }

    public static void runWithinMapperLog(Mapper mapper, String methodName, Runnable run) {
        callWithinMapperLog(mapper, methodName, () -> {
            run.run();
            return null;
        });
    }
}
