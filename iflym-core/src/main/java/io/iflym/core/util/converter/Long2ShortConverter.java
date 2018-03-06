package io.iflym.core.util.converter;

/**
 * 实现从long到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2ShortConverter implements Converter<Long, Short> {
    public static final Long2ShortConverter INSTANCE = new Long2ShortConverter();

    @Override
    public Short apply(Long value) {
        return value.shortValue();
    }
}
