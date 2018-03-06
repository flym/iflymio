package io.iflym.core.util.converter;

/**
 * 实现从int到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2FloatConverter implements Converter<Integer, Float> {
    public static final Integer2FloatConverter INSTANCE = new Integer2FloatConverter();

    @Override
    public Float apply(Integer integer) {
        return integer.floatValue();
    }
}
