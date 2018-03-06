package io.iflym.core.util.converter;

/**
 * 实现从float到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2ShortConverter implements Converter<Float, Short> {
    public static final Float2ShortConverter INSTANCE = new Float2ShortConverter();

    @Override
    public Short apply(Float value) {
        return value.shortValue();
    }
}
