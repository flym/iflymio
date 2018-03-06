package io.iflym.mybatis.criteria;

import com.google.common.collect.ImmutableList;
import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.criteria.criterion.*;
import io.iflym.mybatis.criteria.criterion.subquery.ExistsSubCriterion;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 用于描述指定的条件信息
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public interface Criterion extends Cloneable {
    /**
     * 为相应的条件绑定条件源
     *
     * @param criteria 相应的查询源对象
     */
    void injectCriteria(Criteria criteria);

    /**
     * 必要的初始化
     * 此方法将在相应的属性均设置完毕之后处理
     */
    default void init() {
        //nothing to do
    }

    /**
     * 返回相应的sql形式
     *
     * @return 此条件相对应的sql
     */
    String toSql();

    /**
     * 返回相应的参数信息
     *
     * @return 此条件中所使用到的参数值列表
     */
    List<ParamValue> fetchParams();

    /**
     * 克隆对象,新对象可以修改自己的状态,不影响原对象
     *
     * @return 返回一个新的查询对象
     */
    Criterion clone();

    //---------------------------- 静态构建块开始 ------------------------------//

    /**
     * = 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值
     * @return 具体的条件对象
     */
    static <T> Criterion eq(String property, T value) {
        return new EqCriterion<>(property, value);
    }

    /**
     * = 条件或is null
     *
     * @param property 实体属性
     * @param value    此属性对应的值, 如果为null,则采用isNull条件
     * @return 具体的条件对象
     */
    static <T> Criterion eqOrNull(String property, T value) {
        return ObjectUtils.isEmpty(value) ? isNull(property) : eq(property, value);
    }

    /**
     * >= 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值
     * @return 具体的条件对象
     */
    static <T> Criterion ge(String property, T value) {
        return new GeCriterion<>(property, value);
    }

    /**
     * > 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值
     * @return 具体的条件对象
     */
    static <T> Criterion gt(String property, T value) {
        return new GtCriterion<>(property, value);
    }

    /**
     * <= 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值
     * @return 具体的条件对象
     */
    static <T> Criterion le(String property, T value) {
        return new LeCriterion<>(property, value);
    }

    /**
     * < 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值
     * @return 具体的条件对象
     */
    static <T> Criterion lt(String property, T value) {
        return new LtCriterion<>(property, value);
    }

    /**
     * like 条件
     *
     * @param property 实体属性
     * @param value    此属性对应的值,不要自行带%号
     * @param likeType like的类型
     * @return 具体的条件对象
     */
    static Criterion like(String property, String value, LikeTypeValue likeType) {
        return new LikeCriterion(property, value, likeType);
    }

    /**
     * between 条件
     *
     * @param property   实体属性
     * @param startValue 起始值
     * @param endValue   结束值
     * @return 具体的条件对象
     */
    static <T> Criterion between(String property, T startValue, T endValue) {
        return new BetweenCriterion<>(property, startValue, endValue);
    }

    /**
     * in 条件
     *
     * @param property 实体属性
     * @param values   此属性的值的集合,为一个可变数组
     * @return 具体的条件对象
     */
    @SafeVarargs
    static <T> Criterion in(String property, T... values) {
        return new InCriterion<>(property, Arrays.asList(values));
    }

    /**
     * in 条件,传集合条件
     *
     * @param property 实体属性
     * @param values   此属性的值的集合
     * @return 具体的条件对象
     */
    static <T> Criterion in(String property, Collection<T> values) {
        return new InCriterion<>(property, values);
    }

    /**
     * is null 条件
     *
     * @param property 实体属性
     * @return 具体的条件对象
     */
    static Criterion isNull(String property) {
        return new NullCriterion(property);
    }

    /**
     * is not null 条件
     *
     * @param property 实体属性
     * @return 具体的条件对象
     */
    static Criterion notNull(String property) {
        return new NotNullCriterion(property);
    }

    /**
     * 属性相等性条件 如 a.x = a.y
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion eqProperty(String propertyA, String propertyB) {
        return new EqPropertyCriterion(propertyA, propertyB);
    }

    /**
     * a.x >= a.y 条件
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion geProperty(String propertyA, String propertyB) {
        return new GePropertyCriterion(propertyA, propertyB);
    }

    /**
     * a.x > a.y 条件
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion gtProperty(String propertyA, String propertyB) {
        return new GtPropertyCriterion(propertyA, propertyB);
    }

    /**
     * a.x <= a.y 条件
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion leProperty(String propertyA, String propertyB) {
        return new LePropertyCriterion(propertyA, propertyB);
    }

    /**
     * a.x < a.y 条件
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion ltProperty(String propertyA, String propertyB) {
        return new LtPropertyCriterion(propertyA, propertyB);
    }

    /**
     * != 条件
     *
     * @param property 相应的实体属性
     * @param value    具体的属性的值
     * @return 具体的条件对象
     */
    static <T> Criterion notEq(String property, T value) {
        return new NotEqCriterion<>(property, value);
    }

    /**
     * != 条件 或 is not null
     *
     * @param property 相应的实体属性
     * @param value    具体的属性的值,如果为null,则使用 is not null条件
     * @return 具体的条件对象
     */
    static <T> Criterion notEqOrNotNull(String property, T value) {
        return ObjectUtils.isEmpty(value) ? notNull(property) : notEq(property, value);
    }

    /**
     * a.x != a.y 条件
     *
     * @param propertyA 左侧的实体属性
     * @param propertyB 右侧的实体属性
     * @return 具体的属性条件对象
     */
    static PropertyCriterion notEqProperty(String propertyA, String propertyB) {
        return new NotEqPropertyCriterion(propertyA, propertyB);
    }

    /**
     * (a and b) 条件
     *
     * @param criterionA 左侧的条件对象
     * @param criterionB 右侧的条件对象
     * @return 组合起来的条件对象
     */
    static Criterion and(Criterion criterionA, Criterion criterionB) {
        return and(ImmutableList.of(criterionA, criterionB));
    }

    /**
     * (a and b and c) 条件
     *
     * @param criterions 多个条件对象,为一个可变数组
     * @return 组合起来的条件对象
     */
    static Criterion and(Criterion... criterions) {
        return and(Arrays.asList(criterions));
    }

    /**
     * (a and b and c) 条件 集合版
     *
     * @param criterions 相应的多个条件的集合
     * @return 组合起来的条件对象
     */
    static Criterion and(Collection<Criterion> criterions) {
        return new AndCriterion(criterions);
    }

    /**
     * (a or b) 条件
     *
     * @param criterionA 左侧的条件对象
     * @param criterionB 右侧的条件对象
     * @return 组合起来的条件对象
     */
    static Criterion or(Criterion criterionA, Criterion criterionB) {
        return or(ImmutableList.of(criterionA, criterionB));
    }

    /**
     * (a or b or c) 条件
     *
     * @param criterions 多个条件对象,为一个可变数组
     * @return 组合起来的条件对象
     */
    static Criterion or(Criterion... criterions) {
        return or(Arrays.asList(criterions));
    }

    /**
     * (a or b or c) 条件 集合版
     *
     * @param criterions 相应的多个条件的集合
     * @return 组合起来的条件对象
     */
    static Criterion or(Collection<Criterion> criterions) {
        return new OrCriterion(criterions);
    }

    /**
     * not(a) 条件
     *
     * @param criterion 原来的条件对象
     * @return 处理之后的条件对象
     */
    static Criterion not(Criterion criterion) {
        return new NotCriterion(criterion);
    }

    /**
     * 原生的sql 条件
     *
     * @param sql 原始的sql条件语句
     * @return 构建出来的条件对象
     */
    static Criterion sql(String sql) {
        return new SqlCriterion(sql);
    }

    /**
     * 原生的sql条件，并且带相应的参数信息
     *
     * @param sql           原始的sql条件语句
     * @param typeValueList 此sql所对应的?占位符的条件值集合
     * @return 构建出来的条件对象
     */
    static Criterion sql(String sql, List<ParamValue> typeValueList) {
        return new SqlCriterion(sql, typeValueList);
    }

    /**
     * 描述一个存在性的条件
     *
     * @param criteria exists里面的存在性条件对象
     * @return 构建出来的存在性条件对象
     */
    static Criterion exists(Criteria criteria) {
        return new ExistsSubCriterion(criteria);
    }

    //---------------------------- 静态构建块结束 ------------------------------//
}