package io.iflym.core.util.converter;

/**
 * 实现从long到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2FloatConverter implements Converter<Long, Float> {
    public static final Long2FloatConverter INSTANCE = new Long2FloatConverter();

    @Override
    public Float apply(Long value) {
        return value.floatValue();
    }
}
