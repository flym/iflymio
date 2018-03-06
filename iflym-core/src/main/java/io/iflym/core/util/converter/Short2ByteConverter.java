package io.iflym.core.util.converter;

/**
 * 实现从short到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2ByteConverter implements Converter<Short, Byte> {
    public static final Short2ByteConverter INSTANCE = new Short2ByteConverter();

    @Override
    public Byte apply(Short value) {
        return value.byteValue();
    }
}
