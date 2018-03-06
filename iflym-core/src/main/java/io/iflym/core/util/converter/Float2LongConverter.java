package io.iflym.core.util.converter;

/**
 * 实现从float到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2LongConverter implements Converter<Float, Long> {
    public static final Float2LongConverter INSTANCE = new Float2LongConverter();

    @Override
    public Long apply(Float value) {
        return value.longValue();
    }
}
