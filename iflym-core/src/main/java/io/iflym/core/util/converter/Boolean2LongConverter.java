package io.iflym.core.util.converter;

/**
 * 实现从bool到long的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Boolean2LongConverter implements Converter<Boolean, Long> {
    public static final Boolean2LongConverter INSTANCE = new Boolean2LongConverter();

    @Override
    public Long apply(Boolean value) {
        return value ? 1L : 0L;
    }
}
