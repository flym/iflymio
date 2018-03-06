/* Created by flym at 2015/7/16 */
package io.iflym.core.util.converter;

/**
 * 修正默认实现，以支持从浮点数进行转换
 *
 * @author flym
 */
public class String2LongConverter implements Converter<String, Long> {

    @Override
    public Long apply(String source) {
        try{
            return Long.valueOf(source);
        } catch(NumberFormatException e) {
            return Double.valueOf(source).longValue();
        }
    }
}
