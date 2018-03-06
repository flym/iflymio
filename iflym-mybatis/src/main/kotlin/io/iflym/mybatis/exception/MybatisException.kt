package io.iflym.mybatis.exception

/** Created by flym on 2017/11/17. */

class MybatisException @JvmOverloads constructor(message: String? = null, e: Throwable? = null) : RuntimeException(message, e)