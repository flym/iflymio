package io.iflym.mybatis.criteria.order;

import io.iflym.core.util.ExceptionUtils;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Order;
import io.iflym.mybatis.criteria.util.PropertyUtils;
import io.iflym.mybatis.domain.info.ColumnInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 描述排序信息
 * Created by flym on 6/11/2016.
 *
 * @author flym
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"property", "asc"})
public class NameOrder implements Order {
    /** 相应的属性信息 */
    @Getter
    private final String property;
    /** 是否升序 */
    @Getter
    private final boolean asc;

    /** 定义查询源 */
    protected Criteria criteria;

    /** 实际的查询源 */
    private transient Criteria queryCriteria;
    /** 实际的查询属性 */
    @SuppressWarnings("FieldCanBeLocal")
    private transient String queryProperty;
    /** 实际的查询列信息 */
    private transient ColumnInfo queryColumn;

    @Override
    public void injectCriteria(Criteria criteria) {
        if(this.criteria == null) {
            this.criteria = criteria;
        }
    }

    @Override
    public void init() {
        val cv2 = PropertyUtils.splitProperty(property);
        queryCriteria = criteria.findCriteria(cv2.t1);
        queryProperty = cv2.t2;
        queryColumn = queryCriteria.getEntityInfo().getColumn(queryProperty);
    }

    @Override
    public Order clone() {
        return ExceptionUtils.doFunRethrowE(() -> (Order) super.clone());
    }

    @Override
    public String toSql() {
        String sql = queryCriteria.getAlias() + DOT + queryColumn.getColumnName();

        return sql + S + (asc ? ASC : DESC);
    }
}
