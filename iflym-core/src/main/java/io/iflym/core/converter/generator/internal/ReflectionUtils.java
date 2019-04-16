package io.iflym.core.converter.generator.internal;

import lombok.val;

import java.lang.reflect.Field;

/**
 * 参考自 {@link org.springframework.util.ReflectionUtils} 有部分调整
 * created at 2019-04-16
 *
 * @author flym
 */
public class ReflectionUtils {
    private static final String SPRING_CLASS_REFLECTION_UTILS = "org.springframework.util.ReflectionUtils";
    private static final boolean SPRING_CLASS_REFLECTION_UTILS_EXIST = ClassUtils.isPresent(SPRING_CLASS_REFLECTION_UTILS, ReflectionUtils.class.getClassLoader());

    public static Field findField(Class clazz, String name) {
        if(SPRING_CLASS_REFLECTION_UTILS_EXIST && SpringOptions.springClassExist4Test) {
            return SpringReflectionUtils.findField(clazz, name);
        }

        Class<?> searchType = clazz;
        while(Object.class != searchType) {
            val fields = searchType.getDeclaredFields();
            for(Field field : fields) {
                if(name.equals(field.getName())) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static class SpringReflectionUtils {
        private static Field findField(Class clazz, String name) {
            return org.springframework.util.ReflectionUtils.findField(clazz, name);
        }
    }
}
