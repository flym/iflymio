package io.iflym.mybatis.criteria.util;

import io.iflym.core.tuple.Tuple2;
import io.iflym.mybatis.mapperx.Sqls;

/**
 * 针对属性的处理工具
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public class PropertyUtils {
    /** 将相应的查询属性拆分为查询源和查询属性 */
    public static Tuple2<String, String> splitProperty(String property) {
        int index = property.indexOf(Sqls.DOT);
        if(index == -1) {
            return new Tuple2<>(null, property);
        }

        return new Tuple2<>(property.substring(0, index), property.substring(index + 1));
    }
}
