package io.iflym.mybatis.domain.field.json;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * created at 2018-05-31
 *
 * @author flym
 */
public class JsonedSupportedResultSetHandlerUtils {
    //---------------------------- 访问内部字段 start ------------------------------//
    private static Class clazz;

    static {
        try{
            clazz = ClassUtils.forName("org.apache.ibatis.executor.resultset.DefaultResultSetHandler.UnMappedColumnAutoMapping", JsonedSupportedResultSetHandler.class.getClassLoader());
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("并没有找到类:DefaultResultSetHandler.UnMappedColumnAutoMapping");
        }
    }

    public static final Field TYPE_HANDLER_FIELD = ReflectionUtils.findField(clazz, "typeHandler");
    public static final Field COLUMN_FIELD = ReflectionUtils.findField(clazz, "column");
    public static final Field PRIMITIVE_FIELD = ReflectionUtils.findField(clazz, "primitive");
    public static final Field PROPERTY_FIELD = ReflectionUtils.findField(clazz, "property");

    static {
        ReflectionUtils.makeAccessible(TYPE_HANDLER_FIELD);
        ReflectionUtils.makeAccessible(COLUMN_FIELD);
        ReflectionUtils.makeAccessible(PRIMITIVE_FIELD);
        ReflectionUtils.makeAccessible(PROPERTY_FIELD);
    }

    //---------------------------- 访问内部字段 start ------------------------------//

    //---------------------------- 供替代方法访问部分 start ------------------------------//

    public static TypeHandler getTypeHandler(Object obj) {
        return (TypeHandler) ReflectionUtils.getField(TYPE_HANDLER_FIELD, obj);
    }

    public static String getColumn(Object obj) {
        return (String) ReflectionUtils.getField(COLUMN_FIELD, obj);
    }

    public static boolean getPrimitive(Object obj) {
        return (Boolean) ReflectionUtils.getField(PRIMITIVE_FIELD, obj);
    }

    public static String getProperty(Object obj) {
        return (String) ReflectionUtils.getField(PROPERTY_FIELD, obj);
    }

    //---------------------------- 供替代方法访问部分 end ------------------------------//
}
