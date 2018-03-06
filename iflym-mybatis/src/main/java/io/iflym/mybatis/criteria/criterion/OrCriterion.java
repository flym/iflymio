package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.Criterion;

import java.util.Collection;

import static io.iflym.mybatis.mapperx.Sqls.OR;

/**
 * 描述or的联合操作
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class OrCriterion extends Junction {
    public OrCriterion(Collection<Criterion> criterions) {
        super(OR, criterions);
    }
}
