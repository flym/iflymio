/* Created by flym at 11/20/16 */
package io.iflym.core.util.converter;

import java.util.Date;

/**
 * 实现将日期类型转换为长整型
 *
 * @author flym
 */
public class Date2LongConverter implements Converter<Date, Long> {
    @Override
    public Long apply(Date source) {
        //采用默认的格式
        return source.getTime();
    }
}
