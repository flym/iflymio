package io.iflym.mybatis.domain.field.json.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.iflym.mybatis.domain.field.json.Jsoned


@Suppress("UNCHECKED_CAST")
class JsonedSerializer<T> : StdSerializer<Jsoned<T>>(Jsoned::class.java as Class<Jsoned<T>>) {
    override fun serialize(value: Jsoned<T>, gen: JsonGenerator?, provider: SerializerProvider) {
        //不需要判断value为null, 此已被处理
        provider.defaultSerializeValue(value.t1, gen)
    }
}