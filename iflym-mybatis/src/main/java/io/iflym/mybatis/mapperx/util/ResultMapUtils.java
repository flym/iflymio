package io.iflym.mybatis.mapperx.util;

import io.iflym.core.util.ObjectUtils;
import lombok.val;
import org.apache.ibatis.mapping.ResultMap;

/**
 * 辅助完成mybatis resultMap的处理
 *
 * @author flym
 * Created by flym on 2017/10/25.
 */
public class ResultMapUtils {
    private static final String RESULT_MAP_INLINE_POSTFIX = "-Inline";

    /**
     * 判断一个resultMap是否是通过 xml中的 resultType属性来生成的,即是一个内联的resultMap
     * 因为生成resultMap的方法有2种,一种是通过标识的 resultMap 标签生成,
     * 一种是在statement中显式地指定resultType,这时候就会产生一个inline的resultMap,这种resultMap里面,只有type,没有其它信息
     *
     * @see org.apache.ibatis.builder.MapperBuilderAssistant#getStatementResultMaps(String, Class, String)
     */
    public static boolean isInlineResultMap(ResultMap resultMap) {
        val id = resultMap.getId();
        val resultMapping = resultMap.getResultMappings();
        val idResultMapping = resultMap.getIdResultMappings();

        //按照参考中的代码所示,一个内联resultMap的id是有特殊规则的,并且相应的结果映射为空
        return id.endsWith(RESULT_MAP_INLINE_POSTFIX)
                && ObjectUtils.isEmpty(resultMapping)
                && ObjectUtils.isEmpty(idResultMapping);
    }
}
