package io.iflym.core.util.converter;

/**
 * 实现从int到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2LongConverter implements Converter<Integer, Long> {
    public static final Integer2LongConverter INSTANCE = new Integer2LongConverter();

    @Override
    public Long apply(Integer integer) {
        return integer.longValue();
    }
}
