package io.iflym.mybatis.example

import io.iflym.core.util.NumberUtils
import io.iflym.core.util.ObjectUtils
import io.iflym.mybatis.domain.Entity
import io.iflym.mybatis.domain.Key
import io.iflym.mybatis.domain.Keyed
import io.iflym.mybatis.domain.UniqueKey
import io.iflym.mybatis.domain.info.ColumnInfo
import io.iflym.mybatis.domain.info.EntityInfoHolder
import io.iflym.mybatis.domain.util.ColumnInfoUtils


/**
 * @author luyi
 * @date 2017/12/7
 */
@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
class Example<T : Entity<T>> private constructor(val entity: T) {

    /** 属性选择器 */
    private val selectorSet: MutableSet<PropertySelector> = mutableSetOf()

    /** 指定哪些属性是必须更新的 */
    private val mustProperties: MutableSet<String> = mutableSetOf()

    /** 使用的惟一组名(如果使用惟一键指定) */
    private var ukGroup: String? = null

    /** 主键 */
    private val key: Key by lazy { entity.key() }

    /** 惟一键 */
    private val ukey: UniqueKey by lazy {
        val group = ukGroup!!
        val entityInfo = EntityInfoHolder.get(entity.javaClass)
        ColumnInfoUtils.getUniqueKey(entityInfo, group, entity)
    }

    /** 获取相应的惟一定位值 */
    fun fetchKeyed(): Keyed = if(ObjectUtils.isEmpty(ukGroup)) key else ukey

    /** 使用对象的惟一键组 */
    fun usingUk(ukGroup: String): Example<T> {
        this.ukGroup = ukGroup
        return this
    }

    /** 添加属性选择器 */
    fun addSelector(selector: PropertySelector): Example<T> {
        this.selectorSet.add(selector)
        return this
    }

    /**
     * 排除值为0的属性
     */
    fun nonZero(): Example<T> {
        addSelector(NotZeroPropertySelector.INSTANCE)
        return this
    }

    /**
     * 排除值为Null的属性
     */
    fun nonNull(): Example<T> {
        addSelector(NotNullPropertySelector.INSTANCE)
        return this
    }

    /**
     * 排除值为null或者0的属性
     */
    fun nonNullAndZero(): Example<T> {
        return this.nonZero().nonNull()
    }

    /**
     * 指定不更新哪些属性
     */
    fun exclude(vararg properties: String): Example<T> {
        return this.addSelector(ExcludePropertySelector(*properties))
    }

    /** 指定额外更新哪些属性，即这些属性是必须更新的 */
    fun must(vararg properties: String): Example<T> {
        mustProperties += properties
        return this
    }

    /** 属性是否允许被更新,取includedProperties,excludedProperties,PropertySelector的交集 */
    private fun isPropertyIncluded(propertyName: String, propertyValue: Any?): Boolean {
        //先判断包含
        //再为选择器
        if(mustProperties.contains(propertyName))
            return true

        return selectorSet.isNotEmpty() && selectorSet.all { it.include(propertyName, propertyValue) }
    }

    /** 根据exampleEntity获取name->value键值对 */
    fun getColumnItemMap(): Map<ColumnInfo, Any?> {
        val entityInfo = EntityInfoHolder.get(entity.javaClass)
        val map = HashMap<ColumnInfo, Any?>()
        entityInfo.columnList.forEach {
            val v = ColumnInfoUtils.get<Any?>(it, entity)
            if(!it.isIdColumn && isPropertyIncluded(it.propertyName, v))
                map[it] = v
        }

        return map
    }

    companion object {
        /** 创建Example,默认选择器为AllPropertySelector（不忽略任何属性） */
        @JvmStatic
        fun <T : Entity<T>> of(entity: T): Example<T> = Example(entity)

        /** 创建Example,默认选择器为AllPropertySelector（不忽略任何属性） */
        @JvmStatic
        fun <T : Entity<T>> ofAll(entity: T): Example<T> = Example(entity).addSelector(AllPropertySelector.INSTANCE)
    }
}

/**
 * 属性选择器接口
 */
interface PropertySelector {
    /**
     * 判断属性是否在更新范围中
     */
    fun include(propertyName: String, propertyValue: Any?): Boolean
}

/**
 * ALL属性选择器
 */
private class AllPropertySelector : PropertySelector {
    override fun include(propertyName: String, propertyValue: Any?): Boolean = true

    companion object {
        val INSTANCE = AllPropertySelector()
    }
}

/**
 * 0属性选择器
 */
private class NotZeroPropertySelector : PropertySelector {
    override fun include(propertyName: String, propertyValue: Any?): Boolean = !NumberUtils.isZero(propertyValue)

    companion object {
        val INSTANCE = NotZeroPropertySelector()
    }
}

/**
 * NotNull属性选择器
 */
private class NotNullPropertySelector : PropertySelector {
    override fun include(propertyName: String, propertyValue: Any?): Boolean = !ObjectUtils.isEmpty(propertyValue)

    companion object {
        val INSTANCE = NotNullPropertySelector()
    }
}

private class ExcludePropertySelector(vararg val excludeProperty: String) : PropertySelector {
    override fun include(propertyName: String, propertyValue: Any?): Boolean = !excludeProperty.contains(propertyName)
}