/* Created by flym at 10/14/16 */
package io.iflym.mybatis.criteria.util;

/**
 * 值工具类，以方便将值转换为sql中的数据
 *
 * @author flym
 */
public class ValueUtils {
    /** 将参数值转换为字符串 */
    public static String value2str(Object value) {
        if(value == null) {
            return "null";
        }

        if(value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }

        return "\'" + value + "\'";
    }
}
