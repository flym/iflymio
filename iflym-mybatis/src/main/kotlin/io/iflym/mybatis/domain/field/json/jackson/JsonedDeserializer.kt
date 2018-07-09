package io.iflym.mybatis.domain.field.json.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.iflym.mybatis.domain.field.json.Jsoned
import java.io.IOException


class JsonedDeserializer<T> : StdDeserializer<Jsoned<T>>(Jsoned::class.java), ContextualDeserializer {
    /** 用于创建相应的反序列化器的上下文  */
    @Transient
    private lateinit var usedContext: DefaultDeserializationContext

    private lateinit var contentType: JavaType

    /**
     * 设置并绑定相应的初始反序列化器处理工厂
     * 因为不同的处理上下文中所使用的反序列化器是可以相通的,因此这里直接使用初始化的context来创建相应的处理器
     * 同时使用全局的缓存来缓存相应的实例,以在后面再次使用
     */
    fun bindObjectMapper(objectMapper: ObjectMapper) {
        val context = objectMapper.deserializationContext as DefaultDeserializationContext
        usedContext = context.createInstance(objectMapper.deserializationConfig, null, objectMapper.injectableValues)
    }

    @Throws(IOException::class)
    private fun _create(javaType: JavaType): JsonDeserializer<*> {
        return usedContext.findRootValueDeserializer(usedContext.typeFactory.constructType(javaType))
    }

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Jsoned<T>? {
        val obj = _create(contentType).deserialize(p, ctxt)

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
        //复制在第一次时使用的上下文信息
        result.usedContext = usedContext

        return result
    }
}