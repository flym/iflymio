package io.iflym.mybatis.domain.info;

import com.google.common.base.CaseFormat;
import io.iflym.mybatis.domain.annotation.Table;
import io.iflym.mybatis.exception.MybatisException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.util.StringUtils;

/**
 * 描述模型的表数据信息
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
@Getter
public class TableInfo {
    /** 原始的表信息注解 */
    @Getter(AccessLevel.NONE)
    private Table table;

    /** 表名 */
    private String tableName;

    public static TableInfo build(Class<?> clazz) {
        val value = new TableInfo();
        value.table = clazz.getAnnotation(Table.class);

        if(value.table == null) {
            throw new MybatisException("类并没有table注解:" + clazz);
        }

        String tableName = value.table.name();
        if(!StringUtils.hasText(tableName)) {
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
        }
        value.tableName = tableName;

        return value;
    }
}
