package io.iflym.mybatis.util

import org.apache.ibatis.executor.Executor
import org.apache.ibatis.executor.parameter.ParameterHandler
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler
import org.apache.ibatis.executor.resultset.ResultSetHandler
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.util.concurrent.atomic.AtomicInteger

object JsonedUtils {
    private val postfix: AtomicInteger = AtomicInteger(10001)

    private val clazz by lazy { jsonedClass(DefaultResultSetHandler::class.java) }
    private val constructor by lazy { jsonedConstructor(clazz) }

    /** 构建一个新的支持jsoned处理的resultSetHandler */
    @Suppress("UNCHECKED_CAST")
    private fun jsonedClass(handlerClass: Class<DefaultResultSetHandler>): Class<ResultSetHandler> {
        val pool = InnerClassPool.defaultPool
        val newName = "org.apache.ibatis.executor.resultset.DefaultResultSetHandler\$Jsoned${postfix.getAndIncrement()}"
        val ctClass = pool.getAndRename(handlerClass.name, newName)

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
        return clazz as Class<ResultSetHandler>

//        val instance = clazz.newInstance() as ResultSetHandler
//
//        //复制所有默认字段值
//        defaultResultSetHandler.javaClass.declaredFields.forEach { f ->
//            ReflectionUtils.makeAccessible(f)
//            val name = f.name
//            val destF = ReflectionUtils.findField(clazz, name)
//            ReflectionUtils.makeAccessible(destF)
//
//            ReflectionUtils.setField(destF, instance, ReflectionUtils.getField(f, defaultResultSetHandler))
//        }
//
//        return instance
    }

    private fun jsonedConstructor(clazz: Class<ResultSetHandler>): Constructor<ResultSetHandler> {
        val c = clazz.getConstructor(Executor::class.java, MappedStatement::class.java, ParameterHandler::class.java,
                ResultHandler::class.java, BoundSql::class.java, RowBounds::class.java)
        ReflectionUtils.makeAccessible(c)
        return c
    }

    fun newJsonedResultSetHandler(executor: Executor, mappedStatement: MappedStatement, parameterHandler: ParameterHandler, resultHandler: ResultHandler<*>?, boundSql: BoundSql,
                                  rowBounds: RowBounds?): ResultSetHandler {
        return constructor.newInstance(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds)
    }
}