package io.iflym.mybatis.domain.field.json.jackson

import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.iflym.mybatis.domain.field.json.Jsoned

/** 封闭对jsoned对象到jackson的转换统一处理 */
object JsonedModule {
    /** 注册相应的模块信息 */
    fun registerJackson(objectMapper: ObjectMapper) {
        val deserializer = JsonedDeserializer<Any>()

        val serializer = JsonedSerializer<Any>()

        val jsonValueModule = SimpleModule()
        @Suppress("UNCHECKED_CAST")
        jsonValueModule.addSerializer(Jsoned::class.java, serializer as JsonSerializer<Jsoned<*>>)
        jsonValueModule.addDeserializer(Jsoned::class.java, deserializer)

        objectMapper.registerModule(jsonValueModule)

        deserializer.bindObjectMapper(objectMapper)
    }
}