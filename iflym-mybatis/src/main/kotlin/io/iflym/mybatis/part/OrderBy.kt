package io.iflym.mybatis.part

import com.google.common.collect.ImmutableSet
import io.iflym.mybatis.criteria.Order
import io.iflym.mybatis.exception.MybatisException
import io.iflym.mybatis.part.KeyWords.ASC
import io.iflym.mybatis.part.KeyWords.DESC
import org.springframework.util.StringUtils
import java.util.regex.Pattern

/**
 * 相应的排序语句
 * Created by flym on 8/28/2016.
 */
class OrderBy(orderBy: String) {

    val orderList: List<Order>

    init {
        val list = arrayListOf<Order>()
        for (part in orderBy.split(BLOCK_SPLIT.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()) {
            val matcher = DIRECTION_SPLIT.matcher(part)

            if (!matcher.find()) {
                throw MybatisException("不正确的排序语句,格式不正确:" + part)
            }

            orderBy.javaClass.kotlin

            var property = matcher.group(1)
            val direction = matcher.group(2)

            // 没有属性,仅只有排序值
            if (DIRECTION_KEYWORDS.contains(property) && direction == null) {
                throw MybatisException("不正确的排序语句:" + part)
            }

            property = StringUtils.uncapitalize(property)//属性名,首字母小写

            val asc = direction == KeyWords.ASC
            list.add(if (asc) Order.asc(property) else Order.desc(property))
        }

        orderList = list.toList()
    }

    companion object {
        private val BLOCK_SPLIT = "(?<=Asc|Desc)(?=[A-Z])"
        private val DIRECTION_SPLIT = Pattern.compile("(.+?)(Asc|Desc)?$")

        private val DIRECTION_KEYWORDS = ImmutableSet.of(ASC, DESC)
    }
}
