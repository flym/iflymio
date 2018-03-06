package io.iflym.core.util.converter;

/**
 * 实现从short到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2IntegerConverter implements Converter<Short, Integer> {
    public static final Short2IntegerConverter INSTANCE = new Short2IntegerConverter();

    @Override
    public Integer apply(Short value) {
        return value.intValue();
    }
}
