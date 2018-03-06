package io.iflym.core.util.converter;

/**
 * 实现从short到bool的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2BooleanConverter implements Converter<Short, Boolean> {
    public static final Short2BooleanConverter INSTANCE = new Short2BooleanConverter();

    @Override
    public Boolean apply(Short value) {
        //只要不为0即为真
        short v = value;
        return v != 0;
    }
}
