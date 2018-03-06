package io.iflym.core.util.converter;

/**
 * 实现从char到bool的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2BooleanConverter implements Converter<Character, Boolean> {
    public static final Char2BooleanConverter INSTANCE = new Char2BooleanConverter();

    @Override
    public Boolean apply(Character c) {
        //如果不为0,则为真
        return c != '\0';
    }
}
