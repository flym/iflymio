/* Created by flym at 12/1/2014 */
package io.iflym.mybatis.util;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * 数据库工具类
 *
 * @author flym
 */
public class DbUtils {
    /** 表名规则，必须由数字，字母或下划线组成 */
    private static final Pattern TABLE_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");

    /** 是否存在指定的数据表(视图) */
    public static boolean existsTable(JdbcTemplate jdbcTemplate, String schema, String table) throws SQLException {
        return jdbcTemplate.execute((ConnectionCallback<Boolean>) connection -> {
            try(ResultSet resultSet = connection.getMetaData().getTables(null, schema, table, null)){
                boolean result = resultSet.next();
                if(result) {
                    return true;
                }

                //以下针对h2数据库作兼容，如果原table为小写形式不能正确获取，则转为大写形式作尝试
                String tableUpperCase = table.toUpperCase();
                if(tableUpperCase.equals(table)) {
                    return false;
                }

                try(ResultSet resultSetUpper = connection.getMetaData().getTables(null, schema, tableUpperCase, null)){
                    result = resultSetUpper.next();
                    return result;
                }
            }
        });
    }

    /** 根据指定的ddl语句创建数据表(或视图) */
    public static void createTable(JdbcTemplate jdbcTemplate, String schema, String table, String createDdl) throws SQLException {
        if(existsTable(jdbcTemplate, schema, table)) {
            return;
        }

        jdbcTemplate.execute(createDdl);
    }
}
