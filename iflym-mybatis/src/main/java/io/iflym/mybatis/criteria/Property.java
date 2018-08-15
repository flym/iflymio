package io.iflym.mybatis.criteria;

import io.iflym.mybatis.criteria.property.*;
import io.iflym.mybatis.mapperx.Sqls;

/**
 * 描述一个具体的查询属性
 * 参考于hibernate的projection语义
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public interface Property extends Cloneable {
    /**
     * 注入此属性对应的查询源
     *
     * @param criteria 相应的查询对象
     */
    void injectCriteria(Criteria criteria);

    /** 必要的初始化 */
    default void init() {
        //nothing to do
    }

    /**
     * 相应的sql形式
     *
     * @return 生成的sql语句
     */
    String toSql();

    /**
     * 克隆出一个新的对象, 以方便修改
     *
     * @return 新的对象
     */
    Property clone();

    /**
     * 查询主查询源所有字段
     *
     * @return 生成的表示当前实体所有属性的属性对象
     */
    static Property root() {
        return new RootProperty();
    }

    /**
     * 查询特定的属性
     *
     * @param property 要查询的特定属性
     * @return 生成的属性对象
     */
    static SingleProperty name(String property) {
        return new NameProperty(property);
    }

    /**
     * 使用相应的sql描述查询属性
     *
     * @param sql 要查询的数据的sql表示
     * @return 生成的属性对象
     */
    static SqlProperty sql(String sql) {
        return new SqlProperty(sql);
    }

    /**
     * 直接返回一个常量值
     *
     * @param value 具体的常量 不要在两边追加类似 ' 这种特殊字符
     * @return 生成的常量值属性对象
     */
    static <T> ValueProperty<T> constant(T value) {
        return new ValueProperty<>(value);
    }

    /**
     * 直接查询常量值1
     *
     * @return 生成的表示常量1的值属性对象
     */
    static ValueProperty<Integer> constantOne() {
        return constant(1);
    }

    /** 聚合查询, count */
    static AggregateProperty count(String property) {
        return new AggregateProperty(Sqls.COUNT, property);
    }

    /** 聚合查询, count distinct */
    static CountDistinctAggregateProperty countDistinct(String property) {
        return new CountDistinctAggregateProperty(property);
    }

    /** 聚合查询 count 1 */
    static CountOneAggregateProperty countOne() {
        return new CountOneAggregateProperty();
    }

    /** 聚合查询, max */
    static AggregateProperty max(String property) {
        return new AggregateProperty(Sqls.MAX, property);
    }

    /** 聚合查询, min */
    static AggregateProperty min(String property) {
        return new AggregateProperty(Sqls.MIN, property);
    }

    /** 聚合查询, avg */
    static AggregateProperty avg(String property) {
        return new AggregateProperty(Sqls.AVG, property);
    }

    /** 聚合查询, sum */
    static AggregateProperty sum(String property) {
        return new AggregateProperty(Sqls.SUM, property);
    }
}
