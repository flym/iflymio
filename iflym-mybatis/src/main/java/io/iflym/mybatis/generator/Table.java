package io.iflym.mybatis.generator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述解析表时的表信息
 * Created by flym on 2017/10/10.
 * @author flym
 */
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Table {
    /**  */
    String tableName;
    /** 表注释 */
    String tableComment;
    /** 类名 */
    String className;
    /** mapper的类名 */
    String mapperName;

    public static Table build(ResultSet rs, String tableName, String className) throws SQLException {
        val table = new Table();

        //表名
        table.tableName = tableName;
        //表注释
        table.tableComment = rs.getString("REMARKS");
        //类名
        table.className = className;
        //mapper类名
        table.mapperName = className + "Mapper";

        return table;
    }
}
