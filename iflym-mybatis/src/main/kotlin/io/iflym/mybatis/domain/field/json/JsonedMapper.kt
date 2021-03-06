package io.iflym.mybatis.domain.field.json

import java.lang.reflect.Field

/**
 * 描述从jsoned对象到字符串之间的转换
 * created at 2018-05-28
 *
 * @author flym
 */
interface JsonedMapper {

    /** 转换为字符串  */
    fun <T> toStr(jsoned: Jsoned<T>): String

    /** 从字符串之后的数据重新转换回对象  */
    fun <T> fromStr(str: String, f: Field): Jsoned<T>
}
