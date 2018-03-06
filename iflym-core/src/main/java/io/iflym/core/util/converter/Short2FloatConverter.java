package io.iflym.core.util.converter;

/**
 * 实现从short到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2FloatConverter implements Converter<Short, Float> {
    public static final Short2FloatConverter INSTANCE = new Short2FloatConverter();

    @Override
    public Float apply(Short value) {
        return value.floatValue();
    }
}
