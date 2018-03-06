package io.iflym.core.util.converter;

/**
 * 实现从bool到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2CharConverter implements Converter<Boolean, Character> {
    public static final Boolean2CharConverter INSTANCE = new Boolean2CharConverter();
    private static final Character ONE = (char) 1;
    private static final Character ZERO = (char) 0;

    @Override
    public Character apply(Boolean value) {
        return value ? ONE : ZERO;
    }
}
