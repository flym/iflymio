package io.iflym.mybatis.domain.field.json

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
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

    override fun <T> fromStr(str: String, f: Field): Jsoned<T> {
        val type: Type = (f.genericType as ParameterizedType).actualTypeArguments[0]

        val javaType: JavaType = objectMapper.typeFactory.constructType(type)
        return Jsoned(objectMapper.readValue<T>(str, javaType))
    }
}