package io.iflym.mybatis.domain.field.json

import io.iflym.core.tuple.Tuple1Holder

/**
 * 通过在数据库中使用json存储值，但在java类中使用相应的对象体系来表示相应的数据信息
 * created at 2018-05-28
 *
 * @author flym
 */
class Jsoned<T>() : Tuple1Holder<T>() {
    constructor(t1: T) : this() {
        this.t1 = t1
    }
}

