/* Created by flym at 11/20/16 */
package io.iflym.core.util.converter;

import io.iflym.core.util.DateUtils;

import java.util.Date;

/**
 * 实现将日期类型转换为字符串
 *
 * @author flym
 */
public class Date2StringConverter implements Converter<Date, String> {
    public static final Date2StringConverter INSTANCE = new Date2StringConverter();

    @Override
    public String apply(Date source) {
        //采用默认的格式
        return DateUtils.formatDateTime(source);
    }
}
