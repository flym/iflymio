package io.iflym.core.util.converter;

/**
 * 实现从short到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2DoubleConverter implements Converter<Short, Double> {
    public static final Short2DoubleConverter INSTANCE = new Short2DoubleConverter();

    @Override
    public Double apply(Short value) {
        return value.doubleValue();
    }
}
