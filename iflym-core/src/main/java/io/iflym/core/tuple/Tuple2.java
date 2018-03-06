package io.iflym.core.tuple;

import lombok.RequiredArgsConstructor;

/**
 * 2对象封装器
 *
 * @author flym
 * Created by flym on 2017/8/30.
 */
@RequiredArgsConstructor
public class Tuple2<T1, T2> {
    public final T1 t1;
    public final T2 t2;
}
