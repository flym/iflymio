package io.iflym.core.tuple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 单对象封装器
 * Created by flym on 2017/8/30.
 *
 * @author flym
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Tuple1<T1> {
    public final T1 t1;
}
