package io.iflym.core.util.converter;

/**
 * 实现从double到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2ByteConverter implements Converter<Double, Byte> {
    public static final Double2ByteConverter INSTANCE = new Double2ByteConverter();

    @Override
    public Byte apply(Double value) {
        return value.byteValue();
    }
}
