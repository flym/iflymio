package io.iflym.mybatis.generator;

import com.google.common.base.CaseFormat;
import io.iflym.core.util.ObjectUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用于描述解析表信息时的列数据信息
 * Created by flym on 2017/10/10.
 *
 * @author flym
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Column {
    /** 字段名(全为小写形式) */
    String columnName;

    /** 字段类型 */
    String columnType;

    /** 针对jdbc层的type值 */
    int columnJdbcType;

    /** 针对mybatis层的type值 */
    JdbcType columnMybatisType;

    /** 字段长度 */
    int columnSize;

    /** 此字段的显示长度, 对应于如 int(11)里面的11,或者是tinyint(1)中的1 */
    @Setter
    int displaySize;

    /** 小数点位数 */
    int decimalDigits;

    /** 字段注释 */
    String columnComment;

    /** 字段属性名 */
    String propertyName;

    /** 字段java类型 */
    Class propertyType;

    /** 是否主键 */
    @Setter
    boolean key;

    /** 是否允许为null */
    boolean nullable;

    private Class calcType() {
        switch(columnType) {
            //oracle特定类型
            case "VARCHAR2":
            case "VARCHAR":
            case "TEXT":
            case "LONGTEXT":
                return String.class;
            case "INT":
                //mysql特定类型
            case "INT UNSIGNED":
                return nullable ? Integer.class : int.class;
            case "BIGINT":
                //mysql中特定类型
            case "BIGINT UNSIGNED":
                return nullable ? Long.class : long.class;
            case "DECIMAL":
                return BigDecimal.class;
            case "TINYINT": {
                if(displaySize == 1) {
                    return nullable ? Boolean.class : boolean.class;
                }
                return nullable ? Integer.class : int.class;
            }
            case "BIT":
                return boolean.class;
            case "TIMESTAMP":
            case "TIMESTAMP(6)":
            case "DATE":
            case "DATETIME":
                return Date.class;
            case "CHAR":
                return String.class;
            default:
                throw new RuntimeException("未知类型:" + columnType);
        }
    }

    public void reCalcPropertyType() {
        this.propertyType = calcType();
    }

    /** 通过针对列元数据的rs对象构建列信息 */
    public static Column build(ResultSet rs) throws SQLException {

        val column = new Column();
        //字段名
        column.columnName = rs.getString("COLUMN_NAME").toLowerCase();
        //字段类型
        column.columnType = rs.getString("TYPE_NAME");
        //针对jdbc层的type值
        column.columnJdbcType = rs.getInt("DATA_TYPE");
        //针对mybatis层的type值
        column.columnMybatisType = JdbcType.forCode(column.columnJdbcType);
        //字段长度
        column.columnSize = rs.getInt("COLUMN_SIZE");
        //小数点位数
        column.decimalDigits = rs.getInt("DECIMAL_DIGITS");
        //字段注释
        column.columnComment = rs.getString("REMARKS");
        //字段属性名
        column.propertyName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column.columnName);
        //字段java类型
        column.propertyType = column.calcType();
        //是否主键 跳过
        //是否允许为null
        column.nullable = rs.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls;

        //修正部分数据
        //列注释,如无则修正为属性名
        if(ObjectUtils.isEmpty(column.columnComment)) {
            column.columnComment = column.propertyName;
        }
        //属性类型,特定的类型

        return column;
    }
}
