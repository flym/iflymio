package io.iflym.core.util.converter;

/**
 * 实现从char到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2IntegerConverter implements Converter<Character, Integer> {
    public static final Char2IntegerConverter INSTANCE = new Char2IntegerConverter();

    @Override
    public Integer apply(Character c) {
        return (int) c;
    }
}
