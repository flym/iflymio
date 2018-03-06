package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.ImmutableList;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.mapperx.Sqls;
import lombok.Getter;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.QUEST;

/**
 * 简单的条件处理表达式
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
@Getter
public class SimpleExpression<T> extends AbstractPropertyCriterion {
    /** 相应的值信息 */
    private final T value;
    /** 相应的操作符 */
    private final String op;

    public SimpleExpression(String property, T value, String op) {
        super(property);
        this.value = value;
        this.op = op;
    }

    @Override
    public String toSql() {
        return queryCriteria.getAlias() + Sqls.DOT + queryColumn.getColumnName() + op + QUEST;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return ImmutableList.of(buildValue(queryColumn, value));
    }
}