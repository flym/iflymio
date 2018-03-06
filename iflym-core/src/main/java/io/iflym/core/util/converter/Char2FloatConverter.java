package io.iflym.core.util.converter;

/**
 * 实现从char到float的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2FloatConverter implements Converter<Character, Float> {
    public static final Char2FloatConverter INSTANCE = new Char2FloatConverter();

    @Override
    public Float apply(Character c) {
        return (float) c;
    }
}
