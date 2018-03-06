package io.iflym.core.util.converter;

/**
 * 实现从short到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Short2CharConverter implements Converter<Short, Character> {
    public static final Short2CharConverter INSTANCE = new Short2CharConverter();

    @Override
    public Character apply(Short value) {
        short v = value;
        return (char) v;
    }
}
