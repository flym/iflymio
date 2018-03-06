package io.iflym.core.util.converter;

/**
 * 实现从double到boolean的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2BooleanConverter implements Converter<Double, Boolean> {
    public static final Double2BooleanConverter INSTANCE = new Double2BooleanConverter();

    @Override
    public Boolean apply(Double value) {
        double v = value;
        return v != .0D;
    }
}
