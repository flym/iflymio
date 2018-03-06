package io.iflym.mybatis.part

import java.util.regex.Pattern

/**
 * 描述查询前缀,即前缀操作语
 * Created by flym on 2017/11/17.
 * @author flym
 * */
class Subject(subject: String?) {
    private val count: Boolean = subject?.let { matches(it, COUNT_BY_TEMPLATE) } ?: false

    fun isCountSubject(): Boolean {
        return count
    }

    companion object {
        val COUNT_BY_TEMPLATE = Pattern.compile("^count.*?By")!!

        private fun matches(subject: String, pattern: Pattern): Boolean = pattern.matcher(subject).find()
    }
}