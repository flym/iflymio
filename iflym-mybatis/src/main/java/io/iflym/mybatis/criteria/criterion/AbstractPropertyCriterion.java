package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.Lists;
import io.iflym.core.util.ConvertUtils;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.domain.info.ColumnInfo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.ibatis.type.JdbcType;

import java.util.Collection;
import java.util.List;

/**
 * 简化相应的实现,用于描述存在一个访问属性的条件
 *
 * @author flym
 * Created by flym on 6/3/2016.
 */
@RequiredArgsConstructor
public abstract class AbstractPropertyCriterion extends AbstractCriterion {
    /** 相应的字段名 */
    protected final String property;

    /** 在使用时实际的查询源 */
    protected transient Criteria queryCriteria;
    /** 在使用时实际使用的查询属性 */
    protected transient String queryProperty;
    /** 在查询时实际使用的查询字段属性 */
    protected transient ColumnInfo queryColumn;

    @Override
    public void init() {
        val cv2 = splitProperty(property);
        queryCriteria = criteria.findCriteria(cv2.t1);
        queryProperty = cv2.t2;
        queryColumn = queryCriteria.getEntityInfo().getColumn(queryProperty);
    }

    /** 构建相应的参数信息 */
    @SuppressWarnings("unchecked")
    protected static <T, V> ParamValue<T> buildValue(Criteria queryCriteria, String queryProperty, V value) {
        ColumnInfo column = queryCriteria.getEntityInfo().getColumn(queryProperty);
        return buildValue(column, value);
    }

    /** 构建相应的参数信息 */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    protected static <T, V> ParamValue<T> buildValue(ColumnInfo queryColumn, V value) {
        return new ParamValue<>(queryColumn.getColumnType(), (Class) queryColumn.getPropertyType(), (T) ConvertUtils.convert(value, queryColumn.getPropertyType()));
    }

    /** 构建相应的多个参数信息 */
    protected static <T, V> List<ParamValue<T>> buildValue(Criteria queryCriteria, String queryProperty, Collection<V> valueList) {
        ColumnInfo column = queryCriteria.getEntityInfo().getColumn(queryProperty);
        return buildValue(column, valueList);
    }

    /** 构建相应的多个参数信息 */
    protected static <T, V> List<ParamValue<T>> buildValue(ColumnInfo queryColumn, Collection<V> valueList) {
        List<ParamValue<T>> list = Lists.newArrayListWithExpectedSize(valueList.size());
        @SuppressWarnings("unchecked")
        Class<T> type = (Class) queryColumn.getPropertyType();
        JdbcType jdbcType = queryColumn.getColumnType();
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class) queryColumn.getPropertyType();
        valueList.stream().map(t -> ConvertUtils.convert(t, type))
                .map(t -> new ParamValue<>(jdbcType, clazz, t))
                .forEach(list::add);

        return list;
    }
}
