/* Created by flym at 2014/6/25 */
package io.iflym.core.util.converter;

import io.iflym.core.util.DateUtils;
import lombok.val;

import java.util.Date;
import java.util.function.Function;

/**
 * 由字符串转换为时间
 *
 * @author flym
 */
public class String2DateConverter implements Converter<String, Date> {
    public static final String2DateConverter INSTANCE = new String2DateConverter();

    @Override
    public Date apply(String s) {
        @SuppressWarnings("unchecked")
        Date result = trys(s, DateUtils::parseDateTime, DateUtils::parseDate, DateUtils::parseTime);

        if(result == null) {
            throw new IllegalArgumentException("错误的时间参数:" + s);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Date trys(String s, Function<String, Date>... funs) {
        for(val f : funs) {
            try{
                return f.apply(s);
            } catch(Exception e) {
                //ignore
            }
        }
        return null;
    }
}
