package io.iflym.core.tuple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 2对象封装器
 *
 * @author flym
 * Created by flym on 2017/8/30.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Tuple2<T1, T2> {
    public final T1 t1;
    public final T2 t2;
}
