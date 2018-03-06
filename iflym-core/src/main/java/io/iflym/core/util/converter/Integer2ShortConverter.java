package io.iflym.core.util.converter;

/**
 * 实现从int到short的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2ShortConverter implements Converter<Integer, Short> {
    public static final Integer2ShortConverter INSTANCE = new Integer2ShortConverter();

    @Override
    public Short apply(Integer integer) {
        return integer.shortValue();
    }
}
