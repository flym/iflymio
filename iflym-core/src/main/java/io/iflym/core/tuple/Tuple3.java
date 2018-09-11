package io.iflym.core.tuple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 3对象封装器
 * Created by flym on 2017/8/30.
 *
 * @author flym
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Tuple3<T1, T2, T3> {
    public final T1 t1;
    public final T2 t2;
    public final T3 t3;
}
