package io.iflym.core.util.converter;

/**
 * 实现从bool到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2ShortConverter implements Converter<Boolean, Short> {
    public static final Boolean2ShortConverter INSTANCE = new Boolean2ShortConverter();
    private static final Short ONE = (short) 1;
    private static final Short ZERO = (short) 0;

    @Override
    public Short apply(Boolean value) {
        return value ? ONE : ZERO;
    }
}
