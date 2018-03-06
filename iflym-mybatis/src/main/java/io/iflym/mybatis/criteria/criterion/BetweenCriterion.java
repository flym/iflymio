package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.ImmutableList;
import io.iflym.mybatis.criteria.ParamValue;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 描述between and 的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class BetweenCriterion<T> extends AbstractPropertyCriterion {
    /** 起始值 */
    private final T startValue;
    /** 结束值 */
    private final T endValue;

    public BetweenCriterion(String property, T startValue, T endValue) {
        super(property);
        this.startValue = startValue;
        this.endValue = endValue;
    }

    @Override
    public String toSql() {
        //a.x between ? and ?
        return queryCriteria.getAlias() + DOT + queryColumn.getColumnName()
                + BETWEEN + QUEST + AND + QUEST;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return ImmutableList.of(
                buildValue(queryColumn, startValue),
                buildValue(queryColumn, endValue)
        );
    }
}
