package io.iflym.core.tuple;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 包装对2个对象的持有
 *
 * @author flym
 * Created by flym on 2017/8/30.
 */
@NoArgsConstructor
@AllArgsConstructor
public class Tuple2Holder<T1, T2> {
    public T1 t1;
    public T2 t2;
}
