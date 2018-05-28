package io.iflym.mybatis.domain.field.json

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.reflect.Type


/**
 * 默认使用jackson来实现相应的字符串映射的实现器
 * @author flym
 * date 2018-02-25
 */
class DefaultJacksonJsonedMapper(private val objectMapper: ObjectMapper) : JsonedMapper {

    override fun toStr(jsoned: Jsoned<Any>): String {
        return objectMapper.writeValueAsString(jsoned.t1)
    }

    override fun <T> fromStr(str: String): T {
        //todo 这里的类型未成功处理
        val type: Type = Class::class.java

        val javaType: JavaType = objectMapper.typeFactory.constructType(type)
        return objectMapper.readValue<T>(str, javaType)
    }
}