package io.iflym.core.util.converter;

/**
 * 实现从字符到字符串的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Char2StringConverter implements Converter<Character, String> {
    public static final Char2StringConverter INSTANCE = new Char2StringConverter();

    @Override
    public String apply(Character character) {
        return String.valueOf(character.charValue());
    }
}
