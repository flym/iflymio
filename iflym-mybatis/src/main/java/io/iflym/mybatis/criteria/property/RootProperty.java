package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.domain.info.EntityInfo;
import io.iflym.mybatis.domain.info.EntityInfoHolder;

import static io.iflym.mybatis.mapperx.Sqls.DOT;
import static io.iflym.mybatis.mapperx.Sqls.STAR;

/**
 * 只对主查询源进行数据查询
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public class RootProperty extends AbstractProperty {

    @Override
    public String toSql() {
        Criteria root = criteria.rootCriteria();

        EntityInfo domainTable = EntityInfoHolder.get(root.getClazz());
        assert domainTable != null;

        return root.getAlias() + DOT + STAR;
    }
}
