package io.iflym.core.util.converter;

/**
 * 实现从char到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2ByteConverter implements Converter<Character, Byte> {
    public static final Char2ByteConverter INSTANCE = new Char2ByteConverter();

    @Override
    public Byte apply(Character c) {
        return (byte) c.charValue();
    }
}
