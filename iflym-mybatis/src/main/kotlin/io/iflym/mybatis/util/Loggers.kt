package io.iflym.mybatis.util

import org.slf4j.LoggerFactory


object Loggers {
    inline fun <reified T : Any> loggerFor() = LoggerFactory.getLogger(T::class.java)!!

}