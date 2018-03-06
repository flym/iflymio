package io.iflym.core.util.converter;

/**
 * 实现从byte到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2IntegerConverter implements Converter<Byte, Integer> {
    public static final Byte2IntegerConverter INSTANCE = new Byte2IntegerConverter();

    @Override
    public Integer apply(Byte value) {
        return value.intValue();
    }
}
