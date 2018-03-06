package io.iflym.core.util.converter;

/**
 * 实现从long到byte的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2ByteConverter implements Converter<Long, Byte> {
    public static final Long2ByteConverter INSTANCE = new Long2ByteConverter();

    @Override
    public Byte apply(Long value) {
        return value.byteValue();
    }
}
