package io.iflym.mybatis.mapperx.util;

import io.iflym.mybatis.domain.info.ColumnInfo;
import lombok.val;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

/**
 * 构建相应的结果列映射
 * created at 2018-05-31
 *
 * @author flym
 */
public class ResultMappingUtils {
    /** 根据列信息构建出相应的映射列 */
    public static ResultMapping build(Configuration configuration, ColumnInfo t) {
        val resultMappingBuilder = new ResultMapping.Builder(configuration, t.getPropertyName());
        resultMappingBuilder.column(t.getColumnName());
        resultMappingBuilder.javaType(t.getPropertyType());
        resultMappingBuilder.jdbcType(t.getColumnType());
        return resultMappingBuilder.build();
    }
}
