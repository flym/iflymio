package io.iflym.mybatis.domain.field.json.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.iflym.mybatis.domain.field.json.Jsoned
import java.io.IOException


class JsonedDeserializer<T> : StdDeserializer<Jsoned<T>>(Jsoned::class.java), ContextualDeserializer {
    /** 引用原始的objectMapper对象 */
    private lateinit var objectMapper: ObjectMapper

    private lateinit var contentType: JavaType

    /** 设置并绑定相应的初始反序列化器处理工厂 */
    fun bindObjectMapper(objectMapper: ObjectMapper) {
        this.objectMapper = objectMapper
    }

    @Throws(IOException::class)
    private fun create(javaType: JavaType): JsonDeserializer<*> {
        //以下代码详细参考 objectMapper#readValue中的实现
        val context = objectMapper.deserializationContext as DefaultDeserializationContext
        val usedContext = context.createInstance(objectMapper.deserializationConfig, null, objectMapper.injectableValues)
        return usedContext.findRootValueDeserializer(usedContext.typeFactory.constructType(javaType))
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Jsoned<T>? {
        val obj = create(contentType).deserialize(p, ctxt)

        //如果内部解析为null，那么外部也同样直接为null，避免出现Jsoned(null)的情况

        return if (obj == null) null else Jsoned(obj as T)
    }

    @Throws(JsonMappingException::class)
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        //当序列化对象为root时, property为null
        val wrapperType = if (property == null) ctxt.contextualType else property.type

        //按内部的泛型信息为参考
        val contentType = wrapperType.containedType(0)
        val result = JsonedDeserializer<T>()
        result.contentType = contentType
        result.objectMapper = objectMapper

        return result
    }
}