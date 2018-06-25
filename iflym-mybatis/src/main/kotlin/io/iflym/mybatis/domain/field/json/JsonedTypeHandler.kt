package io.iflym.mybatis.domain.field.json

import io.iflym.core.util.ObjectUtils
import io.iflym.mybatis.util.Loggers.loggerFor
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.lang.reflect.Field
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


/**
 * 用于处理jsoned对象到数据库字符串之间的处理
 * created at 2018-05-28
 *
 * @author flym
 */
class JsonedTypeHandler : BaseTypeHandler<Jsoned<*>>() {
    @Throws(SQLException::class)
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: Jsoned<*>, jdbcType: JdbcType?) {
        ps.setString(i, JsonedMapperFactory.registeredJsonedMapper().toStr(parameter))
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String): Jsoned<*>? {
        return toJsoned(rs.getString(columnName), columnName)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): Jsoned<*>? {
        return toJsoned(rs.getString(columnIndex), columnIndex.toString())
    }

    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): Jsoned<*>? {
        return toJsoned(cs.getString(columnIndex), columnIndex.toString())
    }

    @Throws(SQLException::class)
    fun getNullableResult(rs: ResultSet, columnName: String, field: Field): Jsoned<*>? {
        return JsonedFieldHolder.doWith(field, { getNullableResult(rs, columnName) })
    }

    private fun toJsoned(str: String, columnNameOrIndex: String): Jsoned<*>? {
        return if (ObjectUtils.isEmpty(str)) {
            null
        } else {
            val field = JsonedFieldHolder.get()

            if (field == null) {
                log.warn("在处理jsoned值时,并没有发现相应的字段信息.相应的查询列或下标列为:{}", columnNameOrIndex)
                return null
            }

            JsonedMapperFactory.registeredJsonedMapper().fromStr<Jsoned<*>>(str, field)
        }
    }

    companion object {
        private val log = loggerFor<JsonedTypeHandler>()
    }
}
