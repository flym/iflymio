package io.iflym.core.util.converter;

/**
 * 实现从short到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2LongConverter implements Converter<Short, Long> {
    public static final Short2LongConverter INSTANCE = new Short2LongConverter();

    @Override
    public Long apply(Short value) {
        return value.longValue();
    }
}
