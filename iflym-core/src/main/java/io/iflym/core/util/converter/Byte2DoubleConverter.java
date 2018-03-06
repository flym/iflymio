package io.iflym.core.util.converter;

/**
 * 实现从byte到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2DoubleConverter implements Converter<Byte, Double> {
    public static final Byte2DoubleConverter INSTANCE = new Byte2DoubleConverter();

    @Override
    public Double apply(Byte value) {
        return value.doubleValue();
    }
}
