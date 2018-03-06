package io.iflym.mybatis.criteria.criterion;

import com.google.common.collect.ImmutableList;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.PropertyCriterion;
import io.iflym.mybatis.criteria.ParamValue;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.domain.info.EntityInfo;
import lombok.Getter;
import lombok.val;

import java.util.List;

import static io.iflym.mybatis.mapperx.Sqls.DOT;

/**
 * 用于描述属性之间的简单表达式
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
@Getter
public class PropertyExpression extends AbstractPropertyCriterion implements PropertyCriterion {
    /** 右属性 */
    private final String otherProperty;
    /** 相应的操作符 */
    private final String op;

    /** 另一个查询源 */
    private Criteria otherCriteria;

    /** 右属性查询源 */
    private transient Criteria otherQueryCriteria;
    /** 右查询属性 */
    private transient String otherQueryProperty;
    /** 右查询字段(可能为null) */
    private transient ColumnInfo otherQueryColumn;
    /** 右查询字段的sql形式 */
    private transient String otherQuerySqlColumn;

    public PropertyExpression(String propertyA, String otherProperty, String op) {
        super(propertyA);
        this.otherProperty = otherProperty;
        this.op = op;
    }

    @Override
    public void injectOtherCriteria(Criteria otherCriteria) {
        if(this.otherCriteria == null) {
            this.otherCriteria = otherCriteria;
        }
    }

    @Override
    public PropertyExpression clone() {
        return (PropertyExpression) super.clone();
    }

    @Override
    public void init() {
        super.init();

        //初始化另一个查询源
        if(otherCriteria == null) {
            injectOtherCriteria(criteria);
        }

        val cv2B = splitProperty(otherProperty);
        otherQueryCriteria = otherCriteria.findCriteria(cv2B.t1);
        otherQueryProperty = cv2B.t2;

        //这里的查询可能并不是一个真实的数据源,因此相应的table和查询属性均可能没有,如针对一个独立查询的() alias形式处理
        EntityInfo otherDomainTable = otherQueryCriteria.getEntityInfo();
        otherQueryColumn = otherDomainTable == null ? null : otherDomainTable.getColumn(otherQueryProperty);
        otherQuerySqlColumn = otherQueryColumn == null ? otherQueryProperty : otherQueryColumn.getColumnName();
    }

    @Override
    public String toSql() {
        return queryCriteria.getAlias() + DOT + queryColumn.getColumnName() + op + otherQueryCriteria.getAlias() + DOT + otherQuerySqlColumn;
    }

    @Override
    public List<ParamValue> fetchParams() {
        return ImmutableList.of();
    }
}