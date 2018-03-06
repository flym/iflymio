package io.iflym.core.util;

import lombok.experimental.var;

import java.util.List;
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

    /** 简化仅对单个list进行过滤操作的调用 */
    @SuppressWarnings("unchecked")
    public static <S> List<S> filter(List<S> list, Predicate... predicates) {
        var stream = list.stream();
        for(Predicate predicate : predicates) {
            stream = stream.filter(predicate);
        }

        return stream.collect(Collectors.toList());
    }
}
