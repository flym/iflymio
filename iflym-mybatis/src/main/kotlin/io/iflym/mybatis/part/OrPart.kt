package io.iflym.mybatis.part

import io.iflym.mybatis.criteria.Criterion
import org.springframework.util.StringUtils
import java.util.*

/* Created by flym on 8/28/2016. */
class OrPart internal constructor(source: String) {
    /** 当前过滤块中的每一项条件  */
    private val partList = ArrayList<Part>()

    init {
        val split = PartTree.split(source, KeyWords.AND)
        split.filter(StringUtils::hasText).mapTo(partList, ::Part)
    }

    /** 转换为条件表达式  */
    fun toCriterion(parameter: PartParameter): Criterion {
        val criterionList = partList.map { it.toCriterion(parameter) }
        return Criterion.and(criterionList)
    }
}
