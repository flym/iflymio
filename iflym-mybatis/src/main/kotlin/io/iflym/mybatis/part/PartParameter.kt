package io.iflym.mybatis.part

import io.iflym.mybatis.exception.MybatisException


/**
 * 相应的查询条件
 * Created by flym on 8/28/2016.
 */
class PartParameter(_params: Array<Any>) {
    /** 整个查询条件  */
    private val params: Array<Any> = _params

    /** 当前使用的下标  */
    @Transient
    private var useIndex: Int = 0

    private fun validateIdx() {
        if (useIndex >= params.size)
            throw MybatisException("当前获取的参数下标已超传参长度.参数长度:" + params.size + "获取值:" + useIndex)
    }

    /** 获取一个参数,同时递增相应的下标  */
    @Suppress("UNCHECKED_CAST")
    fun <T> acquire(): T {
        validateIdx()
        return params[useIndex++] as T
    }

    /** 尝试获取下一个参数,如果相应的参数为相应的类型,则获取并增加下标,否则返回null  */
    @Suppress("UNCHECKED_CAST")
    fun <T> tryAcquire(clazz: Class<T>): T? {
        validateIdx()

        val value = params[useIndex]
        if (clazz.isInstance(value)) {
            useIndex++
            return value as T
        }

        return null
    }
}
