package io.iflym.mybatis.part

import io.iflym.mybatis.criteria.Criteria
import io.iflym.mybatis.criteria.Property
import io.iflym.mybatis.domain.Entity
import io.iflym.mybatis.exception.MybatisException
import java.util.regex.Pattern
import kotlin.reflect.KClass

/**
 * 查询树
 * Created by flym on 8/28/2016.
 */
class PartTree(source: String) {
    /** 相应的查询前缀  */
    private val subject: Subject
    /** 整个条件及后面部分  */
    private val predicate: Predicate

    init {
        val matcher = PREFIX_TEMPLATE.matcher(source)
        if (!matcher.find()) {
            throw MybatisException("相应的语句并不是有效的查询语句:" + source)
        } else {
            this.subject = Subject(matcher.group(0))
            this.predicate = Predicate(source.substring(matcher.group().length))
        }
    }

    /** 转换为查询条件  */
    fun <T : Entity<*>> toCriteria(entityClass: KClass<T>, params: Array<Any>): Criteria<T> {
        val parameter = PartParameter(params)
        val criteria = Criteria.of(entityClass.java)
        criteria.where(predicate.toCriterion(parameter))
        if (subject.isCountSubject()) {
            criteria.clearSelect().select(Property.sql("count(1)").setAlias("value"))
            return criteria;
        }
        predicate.orderby?.orderList?.forEach { criteria.order(it) }
        return criteria
    }

    companion object {
        /** 匹配词模板,用于分隔关键字  */
        private val KEYWORD_TEMPLATE = "(%s)(?=\\w)"
        /** 所支持查询前缀  */
        private val QUERY_PATTERN = "find|list|get|query"
        /** 查询总数 还未支持 todo  */
        private val COUNT_PATTERN = "count"

        /** 有效前缀(xxxBy)  */
        private val PREFIX_TEMPLATE = Pattern.compile("^($QUERY_PATTERN|$COUNT_PATTERN).*?By")

        internal fun split(text: String, keyword: String): Array<String> {
            val pattern = Pattern.compile(String.format(KEYWORD_TEMPLATE, keyword))
            return pattern.split(text)
        }

        @JvmStatic
        fun isPartTree(source: String) = PREFIX_TEMPLATE.matcher(source).find()

        fun getCountPattern(): String {
            return COUNT_PATTERN;
        }
    }
}