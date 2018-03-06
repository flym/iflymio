/* Created by flym at 2014/6/25 */
package io.iflym.core.util.converter;

import io.iflym.core.util.ObjectUtils;

import java.util.Date;

/**
 * 由字符串转换为时间
 *
 * @author flym
 */
public class String2DateConverter implements Converter<String, Date> {
    @Override
    public Date apply(String source) {
        if(ObjectUtils.isEmpty(source)) {
            return null;
        }

        Date result;

        //进行3次转换 datetime date time

        //todo

        throw new IllegalArgumentException("错误的时间参数:" + source);
    }
}
