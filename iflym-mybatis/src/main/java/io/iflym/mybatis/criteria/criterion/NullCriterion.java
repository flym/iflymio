package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.ImmutableList;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.mapperx.Sqls;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.IS_NULL;

/**
 * 描述为null的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class NullCriterion extends AbstractPropertyCriterion {
    public NullCriterion(String property) {
        super(property);
    }

    @Override
    public String toSql() {
        return queryCriteria.getAlias() + Sqls.DOT + queryColumn.getColumnName() + IS_NULL;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return ImmutableList.of();
    }
}
