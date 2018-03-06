package io.iflym.core.util.converter;

import java.util.function.Function;

/**
 * 描述一个标准的转换器结构
 *
 * @author flym
 * Created by flym on 2017/10/25.
 */
public interface Converter<S, D> extends Function<S, D> {
}
