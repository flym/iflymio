/* Created by flym at 16-3-25 */
package io.iflym.core.util.converter;


import java.util.Date;

/**
 * 将长整型转换为时间
 *
 * @author flym
 */
public class Long2DateConverter implements Converter<Long, Date> {
    @Override
    public Date apply(Long source) {
        return new Date(source);
    }
}
