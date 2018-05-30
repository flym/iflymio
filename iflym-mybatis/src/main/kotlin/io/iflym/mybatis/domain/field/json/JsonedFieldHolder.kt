package io.iflym.mybatis.domain.field.json


import java.lang.reflect.Field

/**
 * 为当前的jsoned字段进行临时性存储或者处理
 * Created by flym on 1/16/2017.
 */
object JsonedFieldHolder {
    /** 相应的持有字段  */
    private val jsonValueField = ThreadLocal<Field?>()

    fun set(field: Field) {
        jsonValueField.set(field)
    }

    fun get(): Field? {
        return jsonValueField.get()
    }

    fun <T> doWith(field: Field, action: () -> T): T {
        jsonValueField.set(field)
        try {
            return action.invoke()
        } finally {
            jsonValueField.remove()
        }
    }
}
