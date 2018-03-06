package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.Criterion;

import java.util.Collection;

import static io.iflym.mybatis.mapperx.Sqls.AND;

/**
 * 描述and的联合操作
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class AndCriterion extends Junction {
    public AndCriterion(Collection<Criterion> criterions) {
        super(AND, criterions);
    }
}