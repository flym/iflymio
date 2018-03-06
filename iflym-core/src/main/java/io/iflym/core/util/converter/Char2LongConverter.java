package io.iflym.core.util.converter;

/**
 * 实现从char到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2LongConverter implements Converter<Character, Long> {
    public static final Char2LongConverter INSTANCE = new Char2LongConverter();

    @Override
    public Long apply(Character c) {
        return (long) c;
    }
}
