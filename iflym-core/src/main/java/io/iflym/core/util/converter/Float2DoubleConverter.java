package io.iflym.core.util.converter;

/**
 * 实现从float到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2DoubleConverter implements Converter<Float, Double> {
    public static final Float2DoubleConverter INSTANCE = new Float2DoubleConverter();

    @Override
    public Double apply(Float value) {
        return value.doubleValue();
    }
}
