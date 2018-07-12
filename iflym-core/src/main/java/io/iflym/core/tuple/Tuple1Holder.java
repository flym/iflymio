package io.iflym.core.tuple;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 单对象持有器,用于方便获取和修改相应的数据值
 *
 * @author flym
 * Created by flym on 2017/10/25.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Tuple1Holder<T1> {
    public T1 t1;
}
