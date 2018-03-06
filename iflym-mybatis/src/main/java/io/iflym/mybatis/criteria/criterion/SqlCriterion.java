package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.ParamValue;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 描述原生sql的条件表达式
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
@RequiredArgsConstructor
public class SqlCriterion extends AbstractCriterion {
    /** 相应的sql语句 */
    private final String sql;
    private List<ParamValue> paramList;

    public SqlCriterion(String sql, List<ParamValue> paramList) {
        this.sql = sql;
        this.paramList = paramList;
    }

    @Override
    public String toSql() {
        return sql;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return paramList;
    }
}
