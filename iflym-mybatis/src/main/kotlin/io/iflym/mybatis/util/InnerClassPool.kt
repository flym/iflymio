package io.iflym.mybatis.util

import javassist.ClassClassPath
import javassist.ClassPool


object InnerClassPool {
    val defaultPool:ClassPool by lazy {
        val pool = ClassPool(null)
        pool.appendSystemPath()
        pool.appendClassPath(ClassClassPath(InnerClassPool::class.java))
        pool
    }
}