package io.iflym.core.tuple;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 包装对3个对象的持有
 *
 * @author flym
 * Created by flym on 2017/8/30.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Tuple3Holder<T1, T2, T3> {
    public T1 t1;
    public T2 t2;
    public T3 t3;
}
