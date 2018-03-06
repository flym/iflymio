package io.iflym.core.util.converter;

/**
 * 实现从byte到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2LongConverter implements Converter<Byte, Long> {
    public static final Byte2LongConverter INSTANCE = new Byte2LongConverter();

    @Override
    public Long apply(Byte value) {
        return value.longValue();
    }
}
