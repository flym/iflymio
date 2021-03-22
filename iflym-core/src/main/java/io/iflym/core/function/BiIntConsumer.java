package io.iflym.core.function;

/**
 * created at 2021-03-20
 *
 * @author flym
 */
@FunctionalInterface
public interface BiIntConsumer<T> {
    void accept(int v, T t);
}
