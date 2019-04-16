package io.iflym.core.converter.generator.internal;

/**
 * copy 自 {@link org.springframework.util.ClassUtils} 有部分删减
 * created at 2019-04-16
 *
 * @author flym
 */
public class ClassUtils {
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try{
            Class.forName(className, true, classLoader);
            return true;
        } catch(Throwable ex) {
            return false;
        }
    }
}
