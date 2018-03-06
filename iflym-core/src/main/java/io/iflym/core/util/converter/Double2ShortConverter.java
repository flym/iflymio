package io.iflym.core.util.converter;

/**
 * 实现从double到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2ShortConverter implements Converter<Double, Short> {
    public static final Double2ShortConverter INSTANCE = new Double2ShortConverter();

    @Override
    public Short apply(Double value) {
        return value.shortValue();
    }
}
