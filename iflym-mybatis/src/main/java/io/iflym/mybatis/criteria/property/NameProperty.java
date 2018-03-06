package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.util.PropertyUtils;
import lombok.val;

import static io.iflym.mybatis.mapperx.Sqls.DOT;

/**
 * 描述指定名称的属性
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public class NameProperty extends AbstractAliasProperty {
    /** 查询的属性信息 */
    private final String property;

    /** 实际的查询源 */
    private transient Criteria queryCriteria;
    /** 实际的查询属性 */
    private transient String queryProperty;

    public NameProperty(String property) {
        this.property = property;
    }

    @Override
    public void init() {
        val cv2 = PropertyUtils.splitProperty(property);
        queryCriteria = criteria.findCriteria(cv2.t1);
        queryProperty = cv2.t2;
    }

    @Override
    protected String toFrontSql() {
        return queryCriteria.getAlias() + DOT + queryProperty;
    }
}
