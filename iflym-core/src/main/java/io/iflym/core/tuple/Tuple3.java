package io.iflym.core.tuple;

import lombok.RequiredArgsConstructor;

/**
 * 3对象封装器
 * Created by flym on 2017/8/30.
 *
 * @author flym
 */
@RequiredArgsConstructor
public class Tuple3<T1, T2, T3> {
    public final T1 t1;
    public final T2 t2;
    public final T3 t3;
}
