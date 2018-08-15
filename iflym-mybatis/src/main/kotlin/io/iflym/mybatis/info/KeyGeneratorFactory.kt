package io.iflym.mybatis.info

import io.iflym.mybatis.domain.KeyGenerator

/**
 * key生成器工厂
 * Created by flym on 2017/12/27.
 *
 * */
object KeyGeneratorFactory {
    private val generatorMap = hashMapOf<Class<*>, KeyGenerator<*, *>>()

    /** 获取相应的类型的实例 */
    @Suppress("UNCHECKED_CAST")
    fun <T : KeyGenerator<*, *>> getKeyGenerator(clazz: Class<T>): T {
        return generatorMap.getOrPut(clazz) {
            val v = clazz.newInstance()
            v.init()
            return v
        } as T
    }
}