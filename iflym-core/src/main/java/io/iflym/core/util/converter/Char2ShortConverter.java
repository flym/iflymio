package io.iflym.core.util.converter;

/**
 * 实现从char到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2ShortConverter implements Converter<Character, Short> {
    public static final Char2ShortConverter INSTANCE = new Char2ShortConverter();

    @Override
    public Short apply(Character c) {
        return (short) c.charValue();
    }
}
