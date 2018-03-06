package io.iflym.mybatis.part

import io.iflym.mybatis.criteria.Criterion
import io.iflym.mybatis.criteria.LikeTypeValue
import org.springframework.util.StringUtils
import java.util.*

/**
 * 查询一个部分的查询语句
 * Created by flym on 8/28/2016.
 */
class Part(source: String) {
    val property: String
    val type: Type

    init {
        this.type = Type.fromProperty(source)
        this.property = type.extractProperty(source)
    }

    /** 返回相应的条件式  */
    fun toCriterion(parameter: PartParameter): Criterion {
        when (type) {
            Type.BETWEEN -> return Criterion.between(property, parameter.acquire(), parameter.acquire<Any>())
            Type.IS_NULL -> return Criterion.isNull(property)
            Type.IS_NOT_NULL -> return Criterion.notNull(property)
            Type.LESS_THAN -> return Criterion.lt(property, parameter.acquire<Any>())
            Type.LESS_THAN_EQUAL -> return Criterion.le(property, parameter.acquire<Any>())
            Type.GREATER_THAN -> return Criterion.gt(property, parameter.acquire<Any>())
            Type.GREATER_THAN_EQUAL -> return Criterion.ge(property, parameter.acquire<Any>())
            Type.LIKE -> {
                val param = parameter.acquire<String>()
                val likeType = parameter.tryAcquire(LikeTypeValue::class.java) ?: LikeTypeValue.EXACT

                return Criterion.like(property, param, likeType)
            }
            Type.NOT_LIKE -> {
                val param = parameter.acquire<String>()
                val likeType = parameter.tryAcquire(LikeTypeValue::class.java) ?: LikeTypeValue.EXACT

                return Criterion.not(Criterion.like(property, param, likeType))
            }
            Type.IN -> {
                val param = parameter.acquire<Collection<*>>()
                return Criterion.`in`(property, param)
            }
            Type.NOT_IN -> {
                val param = parameter.acquire<Collection<*>>()
                return Criterion.not(Criterion.`in`(property, param))
            }
            Type.TRUE -> return Criterion.eq(property, java.lang.Boolean.TRUE)
            Type.FALSE -> return Criterion.eq(property, java.lang.Boolean.FALSE)
            Type.NEGATING_SIMPLE_PROPERTY -> return Criterion.notEqOrNotNull(property, parameter.acquire<Any>())
            Type.SIMPLE_PROPERTY -> return Criterion.eqOrNull(property, parameter.acquire<Any>())
        }

    }

    /** 各种不同的条件  */
    enum class Type(val numberOfArguments: Int, private vararg val keywords: String) {
        BETWEEN(2, "Between"),
        IS_NULL(0, "Null", "IsNull"),
        IS_NOT_NULL(0, "IsNotNull", "NotNull"),
        LESS_THAN("Lt", "LessThan"),
        LESS_THAN_EQUAL("Le", "LessThanEqual"),
        GREATER_THAN("Gt", "GreaterThan"),
        GREATER_THAN_EQUAL("Ge", "GreaterThanEqual"),
        LIKE("Like"),
        NOT_LIKE("NotLike"),
        IN("In"),
        NOT_IN("NotIn", "IsNotIn"),
        TRUE(0, "True"),
        FALSE(0, "False"),
        NEGATING_SIMPLE_PROPERTY("NotEq", "IsNotEq"),
        /** 相等,默认值  */
        SIMPLE_PROPERTY("Eq", "Is");

        constructor(vararg keywords: String) : this(1, *keywords)

        /** 当前条件是否支持此语句,即此语句是否是按这种条件构建的  */
        protected fun supports(property: String): Boolean = keywords.any { property.endsWith(it) }

        /** 将相应的语句从条件下分离,并返回真实属性  */
        fun extractProperty(part: String): String {
            val candidate = StringUtils.uncapitalize(part)
            return keywords
                    .firstOrNull { candidate.endsWith(it) }
                    ?.let { candidate.substring(0, candidate.length - it.length) }
                    ?: candidate
        }

        companion object {

            /** 按照顺序来列出,以方便在查询定位时按照优先顺序处理,以避免各种后续相同的匹配错误的情况  */
            private val ALL = Arrays.asList(IS_NOT_NULL, IS_NULL, BETWEEN, LESS_THAN, LESS_THAN_EQUAL,
                    GREATER_THAN, GREATER_THAN_EQUAL, NOT_LIKE, LIKE,
                    NOT_IN, IN, TRUE, FALSE, NEGATING_SIMPLE_PROPERTY, SIMPLE_PROPERTY)

            /** 通过原始属性找出相应的条件类型  */
            fun fromProperty(rawProperty: String): Type = ALL.find { it.supports(rawProperty) } ?: SIMPLE_PROPERTY
        }
    }
}
