/* Created by flym at 10/14/16 */
package io.iflym.mybatis.criteria.criterion.subquery;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.criteria.criterion.AbstractCriterion;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 描述一个存在性的子查询条件
 *
 * @author flym
 */
public class ExistsSubCriterion extends AbstractCriterion {
    private Criteria subCriteria;

    public ExistsSubCriterion(Criteria subCriteria) {
        this.subCriteria = subCriteria;
    }

    @Override
    public String toSql() {
        return EXISTS + LP + subCriteria.toSql() + RP;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParamValue> fetchParams() {
        return subCriteria.fetchParams();
    }
}
