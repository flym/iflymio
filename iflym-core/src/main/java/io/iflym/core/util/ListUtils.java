package io.iflym.core.util;

import lombok.experimental.var;
import lombok.val;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 简单的集合处理工具类
 * Created by flym on 2017/11/8.
 *
 * @author flym
 */
public class ListUtils {

    /** 简化仅对单个list进行map转换的调用 */
    @SuppressWarnings("unchecked")
    public static <S, D> List<D> map(List<S> list, Function<S, D> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }

    /** 简化对单个list进行过滤操作，然后返回first的操作 */
    @SafeVarargs
    @Nullable
    public static <T> T findFirst(List<T> list, Predicate<T>... predicates) {
        return findFirstOptional(list, predicates).orElse(null);
    }

    /** 简化对单个list进行过滤操作，然后返回optional first的操作 */
    @SafeVarargs
    @Nonnull
    public static <T> Optional<T> findFirstOptional(List<T> list, Predicate<T>... predicates) {
        var stream = list.stream();
        for(val predicate : predicates) {
            stream = stream.filter(predicate);
        }

        return stream.findFirst();
    }

    /** 简化仅对单个list进行过滤操作的调用 */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <S> List<S> filter(List<S> list, Predicate<S>... predicates) {
        var stream = list.stream();
        for(Predicate predicate : predicates) {
            stream = stream.filter(predicate);
        }

        return stream.collect(Collectors.toList());
    }
}
