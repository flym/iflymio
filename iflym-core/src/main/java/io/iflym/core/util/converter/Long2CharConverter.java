package io.iflym.core.util.converter;

/**
 * 实现从long到char的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Long2CharConverter implements Converter<Long, Character> {
    public static final Long2CharConverter INSTANCE = new Long2CharConverter();

    @Override
    public Character apply(Long value) {
        long v = value;
        return (char) v;
    }
}
