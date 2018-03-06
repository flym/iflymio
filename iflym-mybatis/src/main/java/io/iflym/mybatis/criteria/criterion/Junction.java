package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.Lists;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.mapperx.Sqls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于描述多个条件联合的场景
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class Junction extends AbstractCriterion {
    /** 对应的多个条件值 */
    private final List<Criterion> conditionList = Lists.newArrayList();
    /** 联合操作符 */
    private final String op;

    public Junction(String op, Collection<Criterion> criterions) {
        this.op = op;
        conditionList.addAll(criterions);
    }

    @Override
    public void init() {
        //让里面的每个条件均进行相应的初始化
        conditionList.forEach(Criterion::init);
    }

    @Override
    public void injectCriteria(Criteria criteria) {
        super.injectCriteria(criteria);
        conditionList.forEach(t -> t.injectCriteria(criteria));
    }

    @Override
    public String toSql() {
        //如果只有1个,则不要带上括号
        if(conditionList.size() == 1) {
            return conditionList.get(0).toSql();
        }
        //(a and b and c)
        return conditionList.stream().map(Criterion::toSql)
                .collect(Collectors.joining(op, Sqls.LP, Sqls.RP));
    }

    @Override
    public List<ParamValue> fetchParams() {
        List<ParamValue> list = Lists.newArrayList();
        conditionList.stream().map(Criterion::fetchParams).forEach(list::addAll);
        return list;
    }
}
