package io.iflym.mybatis.util

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor


object ClassPoolUtils {
    /** 冻结类，并且不再可修改  */
    @Throws(Exception::class)
    fun frozen(pool: ClassPool, clazz: String) {
        val ctClass = pool.get(clazz)
        if (!ctClass.isFrozen) {
            ctClass.freeze()
            ctClass.toClass()
        }

        ctClass.debugWriteFile("/tmp")
    }

    /** 替换相应的方法  */
    @Throws(Exception::class)
    fun replaceMethod(pool: ClassPool, clazz: String, method: String, insteadClazz: String, insteadMethod: String, params: Array<String>) {
        val ctClass = pool.get(clazz)
        val insteadClass = pool.get(insteadClazz)

        val paramCts = params.map { pool.get(it) }.toTypedArray()

        val ctMethod = ctClass.getDeclaredMethod(method, paramCts)
        val ctInsteadMethod = insteadClass.getDeclaredMethod(insteadMethod, paramCts)

        ctMethod.setBody(ctInsteadMethod, null)
    }

    /** 添加一个默认的构造函数 */
    fun addDefaultConstructor(ctClass: CtClass) {
        if (ctClass.constructors.any({ it.isConstructor && it.parameterTypes.isEmpty() }))
            return

        val ctConstructor = CtConstructor(emptyArray(), ctClass)
        ctConstructor.setBody("{}")
        ctClass.addConstructor(ctConstructor)
    }
}