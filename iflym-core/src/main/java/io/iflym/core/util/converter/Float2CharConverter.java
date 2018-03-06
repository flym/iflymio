package io.iflym.core.util.converter;

/**
 * 实现从float到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2CharConverter implements Converter<Float, Character> {
    public static final Float2CharConverter INSTANCE = new Float2CharConverter();

    @Override
    public Character apply(Float value) {
        float v = value;
        return (char) v;
    }
}
