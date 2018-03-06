package io.iflym.core.util.converter;

/**
 * 实现从char到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2DoubleConverter implements Converter<Character, Double> {
    public static final Char2DoubleConverter INSTANCE = new Char2DoubleConverter();

    @Override
    public Double apply(Character c) {
        return (double) c;
    }
}
