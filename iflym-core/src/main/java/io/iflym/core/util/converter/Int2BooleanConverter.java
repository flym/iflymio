/* Created by flym at 2015/7/16 */
package io.iflym.core.util.converter;

/**
 * 将int类型转换为boolean类型，大于0为true，否则为false；如果为null则为false
 *
 * @author flym
 */
public class Int2BooleanConverter implements Converter<Integer, Boolean> {

    @Override
    public Boolean apply(Integer source) {
        return source > 0;
    }
}
