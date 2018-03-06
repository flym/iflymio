package io.iflym.core.util.converter;

/**
 * 实现从long到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2DoubleConverter implements Converter<Long, Double> {
    public static final Long2DoubleConverter INSTANCE = new Long2DoubleConverter();

    @Override
    public Double apply(Long value) {
        return value.doubleValue();
    }
}
