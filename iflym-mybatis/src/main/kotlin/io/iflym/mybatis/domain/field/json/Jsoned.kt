package io.iflym.mybatis.domain.field.json

import io.iflym.core.tuple.Tuple1Holder

/**
 * 通过在数据库中使用json存储值，但在java类中使用相应的对象体系来表示相应的数据信息
 * created at 2018-05-28
 *
 * 20180531 备注: 后面本来想使用基于jsoned注解，在column中注入相应的typeHandler来完成相应的处理，这样在domain中就不需要jsoned的包装类型，
 * 而直接使用原始json对象,以减少对框架的依赖，但是在实际操作中，发现此方法可能会对mybatis自身的typeHandler理念相冲突。typeHandler是基于类型处理的，
 * 即它必须使用属性的类型来完成相应的列的映射。如果在domain中，当字段的类型无法对应相应的typeHandler，就会对相应的自身实现相冲突
 *
 * 考虑到此，仍然保留针对jsoned类型的包装处理
 *
 * @author flym
 */
class Jsoned<T>() : Tuple1Holder<T>() {
    constructor(t1: T) : this() {
        this.t1 = t1
    }
}

