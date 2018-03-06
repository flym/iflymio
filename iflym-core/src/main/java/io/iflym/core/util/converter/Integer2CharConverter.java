package io.iflym.core.util.converter;

/**
 * 实现从int到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Integer2CharConverter implements Converter<Integer, Character> {
    public static final Integer2CharConverter INSTANCE = new Integer2CharConverter();

    @Override
    public Character apply(Integer integer) {
        //只要认为不为0即为真
        int i = integer;
        return (char) i;
    }
}
