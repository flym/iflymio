package io.iflym.core.util.converter;

/**
 * 实现从float到int的转换
 * Created by flym on 2017/11/1.
 *
 * @author flym
 */
public class Float2IntegerConverter implements Converter<Float, Integer> {
    public static final Float2IntegerConverter INSTANCE = new Float2IntegerConverter();

    @Override
    public Integer apply(Float value) {
        return value.intValue();
    }
}
