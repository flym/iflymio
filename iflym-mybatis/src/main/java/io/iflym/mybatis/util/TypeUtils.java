package io.iflym.mybatis.util;

/**
 * @author luyi
 * @date  2017/12/4
 */
public class TypeUtils {

    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

}
