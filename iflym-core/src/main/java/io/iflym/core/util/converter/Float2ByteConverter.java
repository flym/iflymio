package io.iflym.core.util.converter;

/**
 * 实现从float到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2ByteConverter implements Converter<Float, Byte> {
    public static final Float2ByteConverter INSTANCE = new Float2ByteConverter();

    @Override
    public Byte apply(Float value) {
        return value.byteValue();
    }
}
