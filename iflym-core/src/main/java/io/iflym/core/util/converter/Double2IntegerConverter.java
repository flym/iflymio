package io.iflym.core.util.converter;

/**
 * 实现从double到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2IntegerConverter implements Converter<Double, Integer> {
    public static final Double2IntegerConverter INSTANCE = new Double2IntegerConverter();

    @Override
    public Integer apply(Double value) {
        return value.intValue();
    }
}
