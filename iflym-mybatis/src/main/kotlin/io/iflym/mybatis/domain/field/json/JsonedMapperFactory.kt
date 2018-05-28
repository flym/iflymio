package io.iflym.mybatis.domain.field.json

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 完成相应的json处理器的工厂处理类
 * created at 2018-05-28
 *
 * @author flym
 */
object JsonedMapperFactory {
    private lateinit var jsonedMapper: JsonedMapper

    fun registerJsonedMapper(jsonedMapper: JsonedMapper) {
        this.jsonedMapper = jsonedMapper
    }

    fun registeredJsonedMapper(): JsonedMapper = jsonedMapper

    /** 注册默认的使用jackson来完成字符串映射的json处理器 */
    fun registerDefaultJacksonJsonedMapper(objectMapper: ObjectMapper) {
        val jsonedMapper = DefaultJacksonJsonedMapper(objectMapper)

        registerJsonedMapper(jsonedMapper)
    }
}
