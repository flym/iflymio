package io.iflym.mybatis.criteria;

import io.iflym.core.tuple.Tuple3Holder;
import org.apache.ibatis.type.JdbcType;

/**
 * 用于描述在一个sql中参数的信息,包括sql类型,java类型以及具体的值
 * <p>
 * 其值信息要求与具体的占位列的类型相匹配
 *
 * @author flym
 * Created by flym on 6/3/2016.
 */
public class ParamValue<T> extends Tuple3Holder<JdbcType, Class<T>, T> {
    public ParamValue() {
    }

    public ParamValue(JdbcType first, Class<T> second, T third) {
        super(first, second, third);
    }
}
