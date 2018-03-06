package io.iflym.core.util.converter;

/**
 * 实现从bool到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2ByteConverter implements Converter<Boolean, Byte> {
    public static final Boolean2ByteConverter INSTANCE = new Boolean2ByteConverter();
    private static final Byte ONE = (byte) 1;
    private static final Byte ZERO = (byte) 0;

    @Override
    public Byte apply(Boolean value) {
        return value ? ONE : ZERO;
    }
}
