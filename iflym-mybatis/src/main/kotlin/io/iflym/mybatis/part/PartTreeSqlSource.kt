package io.iflym.mybatis.part

import io.iflym.mybatis.domain.Entity
import io.iflym.mybatis.exception.MybatisException
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import java.lang.reflect.Method
import kotlin.reflect.KClass

/** Created by flym on 2017/11/17. */
class PartTreeSqlSource(private val method: Method, private val entityClass: KClass<Entity<*>>, private val partTree: PartTree, private val config: Configuration) : SqlSource {
    override fun getBoundSql(parameterObject: Any): BoundSql {
        val criteria = partTree.toCriteria(entityClass, resolveParam(parameterObject))

        return PartTreeBoundSql(criteria, config, parameterObject)
    }

    /**
     * 从mybatis处理过的参数对象中提取原始参数信息
     * 整个流程参照原mybatis关于参数的封装处理
     * @see org.apache.ibatis.reflection.ParamNameResolver.getNamedParams
     */
    private fun resolveParam(parameterObject: Any): Array<Any> {
        val parameterCount = method.parameterTypes.asSequence()
                .filterNot(RowBounds::class.java::isAssignableFrom)
                .filterNot(ResultHandler::class.java::isAssignableFrom)
                .count()

        if (parameterCount == 0)
            return emptyArray()

        if (parameterCount == 1)
            return arrayOf(parameterObject)

        @Suppress("UNCHECKED_CAST")
        val map = parameterObject as? Map<String, *> ?: throw MybatisException("不能识别参数类型信息, 参数为$parameterObject")

        //因为map中参数会按照 param1, param2 ...这样的顺序进行封装,因此直接将相应的数据直接按顺序提取出来即可
        return map.asSequence()
                .filter { (k, _) -> k.length > PARAM_PREFIX_LENGTH && k.startsWith("param") }
                .map { (k, v) -> k.substring(PARAM_PREFIX_LENGTH).toInt() to v }
                .sortedBy { it.first }
                .map { it.second }
                .filterNotNull()
                .toList().toTypedArray()
    }

    companion object {
        /** 参数 paramX的前缀长度值 */
        const val PARAM_PREFIX_LENGTH = 5
    }
}