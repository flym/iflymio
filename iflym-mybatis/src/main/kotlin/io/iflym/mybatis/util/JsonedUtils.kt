package io.iflym.mybatis.util

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler
import org.apache.ibatis.executor.resultset.ResultSetHandler
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Modifier
import java.util.concurrent.atomic.AtomicInteger

object JsonedUtils {
    private val postfix: AtomicInteger = AtomicInteger(10001)

    /** 构建一个新的支持jsoned处理的resultSetHandler */
    fun newJsonedResultSetHandler(defaultResultSetHandler: DefaultResultSetHandler): ResultSetHandler {
        val pool = InnerClassPool.defaultPool
        val newName = "org.apache.ibatis.executor.resultset.DefaultResultSetHandler\$Jsoned${postfix.getAndIncrement()}"
        val ctClass = pool.getAndRename(defaultResultSetHandler.javaClass.name, newName)

        val insteadClazz = "io.iflym.mybatis.domain.field.json.JsonedSupportedResultSetHandler"

        //替换 applyAutomaticMappings 方法
        ClassPoolUtils.replaceMethod(pool, newName, "applyAutomaticMappings", insteadClazz, "applyAutomaticMappings",
                arrayOf("org.apache.ibatis.executor.resultset.ResultSetWrapper", "org.apache.ibatis.mapping.ResultMap", "org.apache.ibatis.reflection.MetaObject", "java.lang.String"))

        //替换 getPropertyMappingValue 方法
        ClassPoolUtils.replaceMethod(pool, newName, "getPropertyMappingValue", insteadClazz, "getPropertyMappingValue",
                arrayOf("java.sql.ResultSet", "org.apache.ibatis.reflection.MetaObject", "org.apache.ibatis.mapping.ResultMapping", "org.apache.ibatis.executor.loader.ResultLoaderMap", "java.lang.String"))

        //添加默认构建方法,以避免构造失败
        ClassPoolUtils.addDefaultConstructor(ctClass)

        //清除所有字段的final属性
        ctClass.declaredFields.forEach { t -> t.modifiers = t.modifiers and Modifier.FINAL.inv() }

        ClassPoolUtils.frozen(pool, newName)

        val clazz = ClassUtils.forName(newName, pool.classLoader)
        val instance = clazz.newInstance() as ResultSetHandler

        //复制所有默认字段值
        defaultResultSetHandler.javaClass.declaredFields.forEach { f ->
            ReflectionUtils.makeAccessible(f)
            val name = f.name
            val destF = ReflectionUtils.findField(clazz, name)
            ReflectionUtils.makeAccessible(destF)

            ReflectionUtils.setField(destF, instance, ReflectionUtils.getField(f, defaultResultSetHandler))
        }

        return instance
    }
}