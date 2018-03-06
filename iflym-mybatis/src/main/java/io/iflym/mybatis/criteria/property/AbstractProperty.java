package io.iflym.mybatis.criteria.property;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Property;

/**
 * 对属性查询信息的主要实现
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public abstract class AbstractProperty implements Property {
    /** 定义查询源 */
    protected Criteria criteria;

    @Override
    public void injectCriteria(Criteria criteria) {
        if(this.criteria == null) {
            this.criteria = criteria;
        }
    }

    @Override
    public Property clone() {
        try{
            return (Property) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
