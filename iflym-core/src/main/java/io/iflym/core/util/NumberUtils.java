package io.iflym.core.util;

/**
 * 数字工具类,用于简化判断数字
 * Created by flym on 2017/12/7.
 *
 * @author flym
 */
public class NumberUtils {
    /**
     * 判断一个数字对象是否是0, 包括整数,浮点数
     * 如果此对象不是数字,则返回false
     */
    public static boolean isZero(Object t) {
        return (t instanceof Number) && Double.doubleToRawLongBits(((Number) t).doubleValue()) == 0;
    }
}
