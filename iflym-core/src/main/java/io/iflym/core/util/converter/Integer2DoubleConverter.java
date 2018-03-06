package io.iflym.core.util.converter;

/**
 * 实现从int到double的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2DoubleConverter implements Converter<Integer, Long> {
    public static final Integer2DoubleConverter INSTANCE = new Integer2DoubleConverter();

    @Override
    public Long apply(Integer integer) {
        return integer.longValue();
    }
}
