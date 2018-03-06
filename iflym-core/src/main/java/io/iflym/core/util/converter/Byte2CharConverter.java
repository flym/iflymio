package io.iflym.core.util.converter;

/**
 * 实现从byte到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2CharConverter implements Converter<Byte, Character> {
    public static final Byte2CharConverter INSTANCE = new Byte2CharConverter();

    @Override
    public Character apply(Byte value) {
        byte v = value;
        return (char) v;
    }
}
