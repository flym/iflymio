package io.iflym.mybatis.domain.util;

import com.google.common.base.CaseFormat;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.iflym.core.util.ConvertUtils;
import io.iflym.core.util.ExceptionUtils;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.UniqueKey;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.domain.info.EntityInfo;
import lombok.val;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 提供对列信息的工具类操作
 * Created by flym on 2017/11/8.
 *
 * @author flym
 */
public class ColumnInfoUtils {
    private static Cache<Field, Method> setterMethodCache = CacheBuilder.newBuilder().weakKeys().build();
    /** 占位符,表示不存在setter方法 */
    private static final Method METHOD_NO_SETTER = ReflectionUtils.findMethod(Object.class, "clone");

    /** 获取相应列属性的值 */
    @SuppressWarnings("unchecked")
    public static <V> V get(ColumnInfo columnInfo, Entity entity) {
        val field = columnInfo.getField();
        ReflectionUtils.makeAccessible(field);
        return (V) ReflectionUtils.getField(field, entity);
    }

    /** 转换相应列属性的值,转换为正确的类型 */
    @SuppressWarnings("unchecked")
    public static <V> V convertValue(ColumnInfo columnInfo, Object value) {
        if(value == null) {
            return null;
        }

        return (V) ConvertUtils.convert(value, columnInfo.getPropertyType());
    }

    /** 设置相应属性的值 */
    public static <V> void set(ColumnInfo columnInfo, Entity entity, V value) {
        val field = columnInfo.getField();
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, entity, value);
    }

    /** 设置相应属性的值,通过调用相应的setter方法(而不是通过字段) */
    public static <V> void setProperty(ColumnInfo columnInfo, Entity entity, V value) {
        val field = columnInfo.getField();
        Method method = ExceptionUtils.doFunRethrowE(() -> setterMethodCache.get(field, () -> {
            val methodName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, columnInfo.getPropertyName());
            val setterMethod = ReflectionUtils.findMethod(entity.getClass(), methodName, columnInfo.getPropertyType());
            return setterMethod == null ? METHOD_NO_SETTER : setterMethod;
        }));

        if(method != METHOD_NO_SETTER) {
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, entity, value);
        }
    }

    /** 获取指定对象指定组的ukey信息 */
    public static UniqueKey getUniqueKey(EntityInfo<?> entityInfo, String group, Entity entity) {
        List<ColumnInfo> columnInfoList = entityInfo.getUniqueIdColumnList(group);
        val values = columnInfoList.stream().map(t -> get(t, entity)).toArray(Object[]::new);
        return UniqueKey.of(group, values);
    }
}
