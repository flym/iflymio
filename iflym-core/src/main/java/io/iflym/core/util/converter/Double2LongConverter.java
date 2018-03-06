package io.iflym.core.util.converter;

/**
 * 实现从double到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2LongConverter implements Converter<Double, Long> {
    public static final Double2LongConverter INSTANCE = new Double2LongConverter();

    @Override
    public Long apply(Double value) {
        return value.longValue();
    }
}
