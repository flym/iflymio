package io.iflym.core.util.converter;

/**
 * 实现从double到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Double2CharConverter implements Converter<Double, Character> {
    public static final Double2CharConverter INSTANCE = new Double2CharConverter();

    @Override
    public Character apply(Double value) {
        double v = value;
        return (char) v;
    }
}
