package io.iflym.core.converter.generator.internal;

import io.iflym.core.util.ExceptionUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * 参考自: {@link org.springframework.beans.BeanUtils} 有调整
 * created at 2019-04-16
 * BeanUtils
 *
 * @author flym
 */
public class BeanUtils {
    private static final String SPRING_CLASS_BEAN_UTILS = "org.springframework.beans.BeanUtils";
    private static final boolean SPRING_CLASS_BEAN_UTILS_EXIST = ClassUtils.isPresent(SPRING_CLASS_BEAN_UTILS, BeanUtils.class.getClassLoader());

    public static PropertyDescriptor[] getPropertyDescriptors(Class clazz) {
        if(SPRING_CLASS_BEAN_UTILS_EXIST && SpringOptions.springClassExist4Test) {
            return SpringBeanUtils.getPropertyDescriptors(clazz);
        }

        return ExceptionUtils.doFunRethrowE(() -> Introspector.getBeanInfo(clazz).getPropertyDescriptors());
    }

    private static class SpringBeanUtils {
        private static PropertyDescriptor[] getPropertyDescriptors(Class clazz) {
            return org.springframework.beans.BeanUtils.getPropertyDescriptors(clazz);
        }
    }
}
