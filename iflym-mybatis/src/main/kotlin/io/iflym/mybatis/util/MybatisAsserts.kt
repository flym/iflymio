package io.iflym.mybatis.util

import io.iflym.core.util.AssertUtils
import io.iflym.mybatis.exception.MybatisException

/**
 * 简化对断言的使用
 * Created by flym on 2017/12/25.
 * @author flym
 *
 * */
object MybatisAsserts {
    @JvmStatic
    fun assertTrue(boolLogic: Boolean, messageTemplate: String, vararg params: Any) {
        AssertUtils.assertTrue(boolLogic, MybatisException::class.java, messageTemplate, params)
    }
}