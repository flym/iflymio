package io.iflym.core.util;

import lombok.val;

/**
 * 断言类, 用于快速判断条件是否满足
 *
 * @author flym
 */
public class AssertUtils {
    private static <T extends Throwable> T createThrowableInstance(Class<T> clazz, String message) {
        return ExceptionUtils.doFunRethrowE(() -> {
            val constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(message);
        });
    }

    /**
     * 断言工具类, 如果逻辑执行不为true,则组装错误信息并throw相应的异常
     *
     * @param boolLogic       要执行的逻辑
     * @param errorClass      期望throw的异常类
     * @param messageTemplate 信息模板
     * @param params          模板参数
     */
    public static <T extends RuntimeException> void assertTrue(boolean boolLogic, Class<T> errorClass, String messageTemplate, Object... params) {
        if(boolLogic) {
            return;
        }

        val message = StringUtils.format(messageTemplate, params);
        throw createThrowableInstance(errorClass, message);
    }
}
