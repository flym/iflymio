package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.criteria.ParamValue;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 描述对一个条件取反的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
@RequiredArgsConstructor
public class NotCriterion extends AbstractCriterion {
    /** 原比较器 */
    private final Criterion criterion;

    @Override
    public void injectCriteria(Criteria criteria) {
        super.injectCriteria(criteria);

        criterion.injectCriteria(criteria);
    }

    @Override
    public String toSql() {
        return NOT + LP + criterion.toSql() + RP;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return criterion.fetchParams();
    }
}
