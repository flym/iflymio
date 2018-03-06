package io.iflym.core.util.converter;

/**
 * 实现从long到boolean的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2BooleanConverter implements Converter<Long, Boolean> {
    public static final Long2BooleanConverter INSTANCE = new Long2BooleanConverter();

    @Override
    public Boolean apply(Long value) {
        //只要不为0即为真
        long v = value;
        return v != 0;
    }
}
