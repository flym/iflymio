package io.iflym.core.util.converter;

/**
 * 实现从long到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2IntegerConverter implements Converter<Long, Integer> {
    public static final Long2IntegerConverter INSTANCE = new Long2IntegerConverter();

    @Override
    public Integer apply(Long value) {
        return value.intValue();
    }
}
