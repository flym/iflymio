package io.iflym.mybatis.part


import io.iflym.mybatis.criteria.Criterion
import java.util.ArrayList

/**
 * 相应的限制条件
 * @author flym
 */
class Predicate(predicate: String) {

    private val whereList = ArrayList<OrPart>()

    val orderby: OrderBy?

    init {
        val parts = PartTree.split(predicate, KeyWords.ORDER_BY)

        if (parts.size > 2) {
            throw IllegalArgumentException("orderby 最多使用一次:$predicate")
        }

        buildWhere(parts[0])
        this.orderby = if (parts.size == 2) OrderBy(parts[1]) else null
    }

    private fun buildWhere(source: String) {
        val split = PartTree.split(source, "Or")
        split.mapTo(whereList) { OrPart(it) }
    }

    fun toCriterion(parameter: PartParameter): Criterion {

        val criterionList = whereList.map { t -> t.toCriterion(parameter) }
        return Criterion.or(criterionList)
    }
}
