package io.iflym.core.util.converter;

/**
 * 实现从float到boolean的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2BooleanConverter implements Converter<Float, Boolean> {
    public static final Float2BooleanConverter INSTANCE = new Float2BooleanConverter();

    @Override
    public Boolean apply(Float value) {
        float v = value;
        return v != .0F;
    }
}
