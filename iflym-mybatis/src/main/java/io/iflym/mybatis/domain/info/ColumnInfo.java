package io.iflym.mybatis.domain.info;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import io.iflym.core.util.ConvertUtils;
import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.DeleteTag;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.UniqueId;
import io.iflym.mybatis.domain.field.json.Jsoned;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

/**
 * 相应的列信息
 * Created by flym on 2017/8/29.
 *
 * @author flym
 */
@Getter
@Slf4j
public class ColumnInfo {
    public static final ColumnInfo UNDEFINED = new ColumnInfo();

    /**
     * 原始的列注解
     */
    @Getter(AccessLevel.NONE)
    private Column column;

    /**
     * 原始的主键标识注解 可能为null
     */
    @Getter(AccessLevel.NONE)
    private Id id;

    /**
     * 删除标记注解 可能为null
     */
    @Getter(AccessLevel.NONE)
    private DeleteTag deleteTag;

    /**
     * UK标记，可能为null
     */
    private UniqueId uniqueId;

    /**
     * 此列所对应的字段信息
     */
    private Field field;

    /**
     * 此列的字段自然顺序
     * 仅用于描述字段之间的前后排序, 不用于其它处理
     */
    private int fieldNaturalOrder;

    /**
     * 列名
     */
    private String columnName;
    /**
     * 列类型
     */
    private JdbcType columnType;
    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 属性类型
     */
    private Class propertyType;

    /** 属性描述 */
    private String propertyDesc;

    /** 删除标记的值 */
    private Object deleteTagVal;

    public static ColumnInfo build(Field field, int fieldNaturalOrder) {
        Column column = field.getAnnotation(Column.class);

        val value = new ColumnInfo();
        value.field = field;
        value.fieldNaturalOrder = fieldNaturalOrder;
        value.column = column;
        value.id = field.getAnnotation(Id.class);

        String columnName = column.name();
        if(!StringUtils.hasText(columnName)) {
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
        value.columnName = columnName;
        JdbcType columnType = column.type();
        if(columnType == JdbcType.NULL) {
            columnType = fromJavaType(field.getType());
        }
        value.columnType = columnType;

        value.propertyName = field.getName();
        value.propertyType = field.getType();
        var propertyDesc = column.comment();
        if(ObjectUtils.isEmpty(propertyDesc)) {
            propertyDesc = field.getName();
        }
        value.propertyDesc = propertyDesc;

        DeleteTag deleteTag = field.getAnnotation(DeleteTag.class);
        value.deleteTag = deleteTag;
        if(null != deleteTag) {
            String orgVal = deleteTag.value();
            value.deleteTagVal = ConvertUtils.convert(orgVal, field.getType());
        }

        value.uniqueId = field.getAnnotation(UniqueId.class);

        return value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 此列是否为主键列
     */
    public boolean isIdColumn() {
        return id != null;
    }

    /**
     * 判断此列是否为UK列
     */
    public boolean isUniqueIdColumn() {
        return uniqueId != null;
    }

    /**
     * 判断此列是否为删除标记列
     */
    public boolean isDeleteTagColumn() {
        return deleteTag != null;
    }

    private static Map<Class, JdbcType> defaultJdbcTypeMap = Maps.newHashMap();

    static {
        defaultJdbcTypeMap.put(int.class, JdbcType.INTEGER);
        defaultJdbcTypeMap.put(Integer.class, JdbcType.INTEGER);

        defaultJdbcTypeMap.put(long.class, JdbcType.NUMERIC);
        defaultJdbcTypeMap.put(Long.class, JdbcType.NUMERIC);

        defaultJdbcTypeMap.put(float.class, JdbcType.FLOAT);
        defaultJdbcTypeMap.put(Float.class, JdbcType.FLOAT);

        defaultJdbcTypeMap.put(double.class, JdbcType.DOUBLE);
        defaultJdbcTypeMap.put(Double.class, JdbcType.DOUBLE);

        defaultJdbcTypeMap.put(boolean.class, JdbcType.BOOLEAN);
        defaultJdbcTypeMap.put(Boolean.class, JdbcType.BOOLEAN);

        defaultJdbcTypeMap.put(byte.class, JdbcType.TINYINT);
        defaultJdbcTypeMap.put(Byte.class, JdbcType.TINYINT);

        defaultJdbcTypeMap.put(short.class, JdbcType.SMALLINT);
        defaultJdbcTypeMap.put(Short.class, JdbcType.SMALLINT);

        defaultJdbcTypeMap.put(BigInteger.class, JdbcType.NUMERIC);
        defaultJdbcTypeMap.put(BigDecimal.class, JdbcType.DECIMAL);

        //字符串
        defaultJdbcTypeMap.put(String.class, JdbcType.VARCHAR);

        //二进制
        defaultJdbcTypeMap.put(byte[].class, JdbcType.BLOB);

        //时间
        defaultJdbcTypeMap.put(Date.class, JdbcType.TIMESTAMP);

        //jsr310
        defaultJdbcTypeMap.put(LocalDate.class, JdbcType.DATE);
        defaultJdbcTypeMap.put(LocalTime.class, JdbcType.TIME);
        defaultJdbcTypeMap.put(LocalDateTime.class, JdbcType.TIMESTAMP);

        //jsoned
        defaultJdbcTypeMap.put(Jsoned.class, JdbcType.VARCHAR);
    }

    private static JdbcType fromJavaType(Class javaType) {
        val result = defaultJdbcTypeMap.get(javaType);
        if(result == null) {
            log.warn("java类型没有被匹配到,将使用默认类型.类型:{}", javaType);
            return JdbcType.OTHER;
        }

        return result;
    }
}
