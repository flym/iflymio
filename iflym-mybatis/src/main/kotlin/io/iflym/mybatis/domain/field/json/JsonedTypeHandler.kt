package io.iflym.mybatis.domain.field.json

import io.iflym.core.util.ObjectUtils
import lombok.extern.slf4j.Slf4j
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType

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
@Slf4j
class JsonedTypeHandler : BaseTypeHandler<Jsoned<*>>() {
    @Throws(SQLException::class)
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: Jsoned<*>, jdbcType: JdbcType) {
        ps.setString(i, JsonedMapperFactory.registeredJsonedMapper().toStr(parameter as Jsoned<Any>))
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String): Jsoned<*>? {
        return toJsoned(rs.getString(columnName))
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): Jsoned<*>? {
        return toJsoned(rs.getString(columnIndex))
    }

    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): Jsoned<*>? {
        return toJsoned(cs.getString(columnIndex))
    }

    private fun toJsoned(str: String): Jsoned<*>? {
        return if (ObjectUtils.isEmpty(str)) {
            null
        } else JsonedMapperFactory.registeredJsonedMapper().fromStr<Jsoned<*>>(str)

    }
}
