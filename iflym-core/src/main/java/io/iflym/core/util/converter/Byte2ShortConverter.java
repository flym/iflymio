package io.iflym.core.util.converter;

/**
 * 实现从byte到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2ShortConverter implements Converter<Byte, Short> {
    public static final Byte2ShortConverter INSTANCE = new Byte2ShortConverter();

    @Override
    public Short apply(Byte value) {
        return value.shortValue();
    }
}
