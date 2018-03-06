package io.iflym.core.util.converter;

/**
 * 实现从byte到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2FloatConverter implements Converter<Byte, Float> {
    public static final Byte2FloatConverter INSTANCE = new Byte2FloatConverter();

    @Override
    public Float apply(Byte value) {
        return value.floatValue();
    }
}
