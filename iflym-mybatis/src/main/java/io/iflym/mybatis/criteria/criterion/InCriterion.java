package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.mapperx.Sqls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 描述in的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class InCriterion<T> extends AbstractPropertyCriterion {
    /** 相应的值信息 要求集合里面不能存在null值,则调用方进行保证 */
    private final Collection<T> values;

    public InCriterion(String property, Collection<T> values) {
        super(property);
        this.values = values;
    }

    @Override
    public String toSql() {
        String param = values.stream().map(t -> Sqls.QUEST).collect(Collectors.joining(COMMA, LP, RP));

        return queryCriteria.getAlias() + DOT + queryColumn.getColumnName() + IN + param;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ParamValue> fetchParams() {
        return (List) buildValue(queryColumn, values);
    }
}
