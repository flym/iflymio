package io.iflym.core.util.converter;

/**
 * 实现从字符串到char之间的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class String2CharConverter implements Converter<String, Character> {
    public static final String2CharConverter INSTANCE = new String2CharConverter();

    @Override
    public Character apply(String s) {
        return s.charAt(0);
    }
}
