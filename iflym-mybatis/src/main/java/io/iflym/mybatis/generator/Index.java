package io.iflym.mybatis.generator;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Ints;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述相应的索引信息
 * Created by flym on 2017/10/10.
 *
 * @author flym
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Index implements Comparable<Index> {
    /** 列名(小写形式) */
    String columnName;
    /** 属性名 */
    String propertyName;
    /** 此列在索引中的位置 */
    int order;

    public static Index build(ResultSet rs) throws SQLException {
        val index = new Index();
        //列名
        index.columnName = rs.getString("COLUMN_NAME").toLowerCase();
        //属性名
        index.propertyName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, index.columnName);
        //列索引位置
        index.order = rs.getInt("KEY_SEQ");

        return index;
    }

    @Override
    public int compareTo(@Nonnull Index o) {
        return Ints.compare(order, o.order);
    }
}
