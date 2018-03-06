package io.iflym.core.util.converter;

/**
 * 实现从bool到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2DoubleConverter implements Converter<Boolean, Double> {
    public static final Boolean2DoubleConverter INSTANCE = new Boolean2DoubleConverter();

    @Override
    public Double apply(Boolean value) {
        return value ? 1D : 0D;
    }
}
