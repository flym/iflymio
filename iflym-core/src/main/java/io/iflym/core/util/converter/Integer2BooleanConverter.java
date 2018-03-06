package io.iflym.core.util.converter;

/**
 * 实现从int到boolean的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2BooleanConverter implements Converter<Integer, Boolean> {
    public static final Integer2BooleanConverter INSTANCE = new Integer2BooleanConverter();

    @Override
    public Boolean apply(Integer integer) {
        //只要认为不为0即为真
        int i = integer;
        return i != 0;
    }
}
