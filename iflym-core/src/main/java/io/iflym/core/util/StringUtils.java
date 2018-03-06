package io.iflym.core.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * 字符串工具类
 *
 * @author flym
 */
public class StringUtils {
    /** 信息格式化, 填充相应的参数信息 */
    public static String format(String template, Object... params) {
        if(params.length == 0) {
            return template;
        }

        return MessageFormatter.arrayFormat(template, params).getMessage();
    }
}
