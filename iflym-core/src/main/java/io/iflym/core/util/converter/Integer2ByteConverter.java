package io.iflym.core.util.converter;

/**
 * 实现从int到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2ByteConverter implements Converter<Integer, Byte> {
    public static final Integer2ByteConverter INSTANCE = new Integer2ByteConverter();

    @Override
    public Byte apply(Integer integer) {
        return integer.byteValue();
    }
}
