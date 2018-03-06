package io.iflym.core.util.converter;

/**
 * 实现从bool到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2IntegerConverter implements Converter<Boolean, Integer> {
    public static final Boolean2IntegerConverter INSTANCE = new Boolean2IntegerConverter();

    @Override
    public Integer apply(Boolean value) {
        return value ? 1 : 0;
    }
}
