package io.iflym.mybatis.domain.field.json;


import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * created at 2018-05-28
 *
 * @author flym
 */
public class JsonedSupportedResultSetHandler {

    private org.apache.ibatis.session.Configuration configuration;
    private static final Object DEFERED = new Object();

    //---------------------------- 占位方法 start ------------------------------//

    private List createAutomaticMappings(org.apache.ibatis.executor.resultset.ResultSetWrapper rsw, org.apache.ibatis.mapping.ResultMap resultMap, org.apache.ibatis.reflection.MetaObject metaObject, String columnPrefix) throws SQLException {
        return null;
    }

    private Object getNestedQueryMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        return null;
    }

    private void addPendingChildRelation(ResultSet rs, MetaObject metaResultObject, ResultMapping parentMapping) throws SQLException {
    }


    private String prependPrefix(String columnName, String prefix) {
        return null;
    }

    //---------------------------- 占位方法 start ------------------------------//

    //---------------------------- 工具方法 start ------------------------------//

    //---------------------------- 工具方法 end ------------------------------//

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        List autoMapping = createAutomaticMappings(rsw, resultMap, metaObject, columnPrefix);

        boolean foundValues = false;
        if(!autoMapping.isEmpty()) {
            for(Iterator iterator = autoMapping.iterator(); iterator.hasNext(); ) {
                Object mapping = iterator.next();
                Object value;
                String column = JsonedSupportedResultSetHandlerUtils.getColumn(mapping);
                ResultSet resultSet = rsw.getResultSet();

                TypeHandler typeHandler = JsonedSupportedResultSetHandlerUtils.getTypeHandler(mapping);
                if(typeHandler instanceof JsonedTypeHandler) {
                    String property = JsonedSupportedResultSetHandlerUtils.getProperty(mapping);
                    Field field = ReflectionUtils.findField(metaObject.getOriginalObject().getClass(), property);
                    value = ((JsonedTypeHandler) typeHandler).getNullableResult(resultSet, column, field);
                } else {
                    value = typeHandler.getResult(resultSet, column);
                }

                if(value != null) {
                    foundValues = true;
                }
                if(value != null || (configuration.isCallSettersOnNulls() && !JsonedSupportedResultSetHandlerUtils.getPrimitive(mapping))) {
                    // gcode issue #377, call setter on nulls (value is not 'found')
                    metaObject.setValue(JsonedSupportedResultSetHandlerUtils.getProperty(mapping), value);
                }
            }
        }
        return foundValues;
    }

    private Object getPropertyMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        if(propertyMapping.getNestedQueryId() != null) {
            return getNestedQueryMappingValue(rs, metaResultObject, propertyMapping, lazyLoader, columnPrefix);
        } else if(propertyMapping.getResultSet() != null) {
            addPendingChildRelation(rs, metaResultObject, propertyMapping);
            return DEFERED;
        } else {
            final TypeHandler<?> typeHandler = propertyMapping.getTypeHandler();
            final String column = prependPrefix(propertyMapping.getColumn(), columnPrefix);

            if(typeHandler instanceof JsonedTypeHandler) {
                String property = propertyMapping.getProperty();
                Field field = ReflectionUtils.findField(metaResultObject.getOriginalObject().getClass(), property);
                return ((JsonedTypeHandler) typeHandler).getNullableResult(rs, column, field);
            } else {
                return typeHandler.getResult(rs, column);
            }
        }
    }
}
