package io.iflym.core.util.converter;

/**
 * 实现从byte到boolean的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Byte2BooleanConverter implements Converter<Byte, Boolean> {
    public static final Byte2BooleanConverter INSTANCE = new Byte2BooleanConverter();

    @Override
    public Boolean apply(Byte value) {
        //值不为0即为真
        byte v = value;
        return v != 0;
    }
}
