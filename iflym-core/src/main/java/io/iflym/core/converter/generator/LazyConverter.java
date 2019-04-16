package io.iflym.core.converter.generator;

import io.iflym.core.util.converter.Converter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

/**
 * 一个lazy初始化的转换器，以避免当转换器并未实际使用时提前创建相应的对象浪费资源
 * created at 2019-04-16
 *
 * @author flym
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LazyConverter<S, D> implements Converter<S, D> {
    private final Supplier<Converter<S, D>> converterSupplier;

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final Converter<S, D> realConverter = converterSupplier.get();

    @Override
    public D apply(S s) {
        return getRealConverter().apply(s);
    }

    public static <S, D> LazyConverter<S, D> build(Supplier<Converter<S, D>> supplier) {
        return new LazyConverter<>(supplier);
    }
}
