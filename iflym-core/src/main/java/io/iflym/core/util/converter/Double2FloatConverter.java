package io.iflym.core.util.converter;

/**
 * 实现从double到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2FloatConverter implements Converter<Double, Float> {
    public static final Double2FloatConverter INSTANCE = new Double2FloatConverter();

    @Override
    public Float apply(Double value) {
        return value.floatValue();
    }
}
