package io.iflym.core.util.converter;

/**
 * 实现从bool到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2FloatConverter implements Converter<Boolean, Float> {
    public static final Boolean2FloatConverter INSTANCE = new Boolean2FloatConverter();

    @Override
    public Float apply(Boolean value) {
        return value ? 1F : 0F;
    }
}
