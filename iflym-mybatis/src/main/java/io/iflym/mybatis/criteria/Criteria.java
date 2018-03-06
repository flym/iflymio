package io.iflym.mybatis.criteria;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Page;
import io.iflym.mybatis.domain.info.EntityInfo;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.mapperx.Sqls;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.iflym.mybatis.mapperx.Sqls.*;

/**
 * 用于描述特定的查询信息
 * 一个查询信息以root入手,关联其它节点,共同形成一个查询信息
 *
 * @author flym
 * Created by flym on 6/3/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Criteria<T extends Entity> implements Cloneable {
    private static final String ROOT_ALIAS_DEFAULT = "rt";
    /** 所对应的查询对象 */
    @Getter
    private Class<T> clazz;

    /** 相应的别名 */
    @Getter
    private String alias;

    /** 相对应的条件信息 */
    private List<Criterion> criterionList;

    /** 查询的属性信息 */
    private List<Property> propertyList;

    /** 相应的排序集 */
    @NonNull
    private List<Order> orderList;

    /** 相应可能的分页条件(limit信息) */
    @Getter
    private Page page = null;

    /** 所有的关联集 */
    protected List<AbstractJoinCriteria<?>> joinList;

    protected Criteria(Class<T> clazz, String alias, List<Criterion> criterionList, List<Property> propertyList, List<Order> orderList) {
        this.clazz = clazz;
        this.alias = alias;
        this.criterionList = criterionList;
        this.propertyList = propertyList;
        this.orderList = orderList;
    }

    /** 构建相应的查询对象 */
    public static <T extends Entity> Criteria<T> of(Class<T> rootClass) {
        return of(rootClass, ROOT_ALIAS_DEFAULT);
    }

    /** 构建相应的查询对象,并使用指定的别名 */
    public static <T extends Entity> Criteria<T> of(Class<T> rootClass, String rootAlias) {
        Criteria<T> criteria = new Criteria<>(rootClass, rootAlias, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());
        //仅root才存储相应的关系
        criteria.joinList = Lists.newArrayList();

        return criteria;
    }

    /** 查询对象的根对象(即当前查询体系的首查询) */
    public Criteria<T> rootCriteria() {
        return this;
    }

    /** 当前查询所对应的表信息 */
    public EntityInfo getEntityInfo() {
        return EntityInfoHolder.get(clazz);
    }

    protected Optional<Criteria> findOptinalCriteria(String alias) {
        if(ObjectUtils.isEmpty(alias)) {
            return Optional.of(this);
        }

        if(Objects.equals(this.alias, alias)) {
            return Optional.of(this);
        }

        //因为关系存储在root上，因此使用root来进行遍列查找
        Criteria<?> root = rootCriteria();
        if(root != this && Objects.equals(root.alias, alias)) {
            return Optional.of(root);
        }

        return root.joinList.stream().filter(t -> Objects.equals(t.getAlias(), alias))
                .findFirst()
                .map(Function.identity());
    }

    /** 根据别名查找到相应的能够执行此处理的源 */
    public final Criteria findCriteria(String alias) {
        Optional<Criteria> optional = findOptinalCriteria(alias);

        return optional.orElseThrow(() -> new RuntimeException("不能被解析的数据源别名:" + alias));
    }

    /** 描述查询条件 */
    public Criteria where(Criterion criterion) {
        Criteria root = rootCriteria();
        if(root != this) {
            root.where(criterion);
            return this;
        }

        criterion.injectCriteria(this);
        criterionList.add(criterion);
        return this;
    }

    /** 添加查询项 */
    public Criteria select(Property property) {
        Criteria root = rootCriteria();
        if(root != this) {
            root.select(property);
            return this;
        }

        property.injectCriteria(this);
        propertyList.add(property);
        return this;
    }

    /** 清除之前的查询 */
    public Criteria clearSelect() {
        rootCriteria().propertyList.clear();

        return this;
    }

    /** 添加排序项 */
    public Criteria order(Order order) {
        Criteria root = rootCriteria();
        if(root != this) {
            root.order(order);
            return this;
        }

        order.injectCriteria(this);
        orderList.add(order);
        return this;
    }

    /** 清除之前的排序项 */
    public Criteria clearOrder() {
        rootCriteria().orderList.clear();

        return this;
    }

    /** 添加限制条件 */
    public Criteria limit(Page page) {
        rootCriteria().page = page;

        return this;
    }

    /** 清除之前的限制条件 */
    public Criteria clearLimit() {
        rootCriteria().page = null;

        return this;
    }

    /** 进行表关联 */
    public final <T2 extends Entity> AbstractJoinCriteria<T2> join(Class<T2> joinClass, String joinAlias, JoinTypeValue joinType, String selfProperty, String joinProperty) {
        return join(joinClass, joinAlias, joinType, Criterion.eqProperty(selfProperty, joinProperty));
    }

    /** 进行表关联,使用指定的条件 */
    public <T2 extends Entity> AbstractJoinCriteria<T2> join(Class<T2> joinClass, String joinAlias, JoinTypeValue joinType, PropertyCriterion onCriterion) {
        Criteria<T> root = rootCriteria();
        EntityJoinCriteria<T2> criteria = new EntityJoinCriteria<>(root, joinClass, joinAlias, joinType, onCriterion);

        root.joinList.add(criteria);
        return criteria;
    }

    /** 进行独立查询的关联,使用指定的条件 */
    @SuppressWarnings("unchecked")
    public <T2 extends Entity> AbstractJoinCriteria<T2> joinCriteria(Criteria<T2> criteria, String joinAlias, JoinTypeValue joinType, PropertyCriterion onCriterion) {
        Criteria<T> root = rootCriteria();
        CriteriaJoinCriteria<T2> criteriaB = new CriteriaJoinCriteria(root, criteria, joinAlias, joinType, onCriterion);

        root.joinList.add(criteriaB);
        return criteriaB;
    }

    /** 创建出一个子查询 */
    public <T3 extends Entity> Criteria<T3> createSubCriteria(Class<T3> subClass, String subAlias) {
        return new SubCriteria<>(subClass, subAlias, rootCriteria());
    }

    /** 生成相应的sql语句 */
    @SuppressWarnings("Duplicates")
    public final String toSql() {
        //仅支持从root进行处理
        Criteria root = rootCriteria();
        if(root != this) {
            return root.toSql();
        }

        StringBuilder sb = new StringBuilder(Sqls.SELECT);

        //如果没有选择任何属性，则默认为rootProperty
        if(propertyList.isEmpty()) {
            select(Property.root());
        }

        propertyList.forEach(Property::init);
        String select = propertyList.stream().map(Property::toSql).collect(Collectors.joining(","));
        sb.append(select);

        sb.append(Sqls.FROM);
        val mainTable = EntityInfoHolder.get(clazz);
        assert mainTable != null;
        sb.append(mainTable.getTable().getTableName()).append(Sqls.S).append(alias);

        if(!ObjectUtils.isEmpty(joinList)) {
            for(AbstractJoinCriteria joinCriteria : joinList) {
                sb.append(joinCriteria.joinType.toSql());
                sb.append(joinCriteria.toJoinRightSql()).append(Sqls.S).append(joinCriteria.getAlias());
                if(joinCriteria.onCriterion != null) {
                    joinCriteria.onCriterion.init();
                    sb.append(ON).append(joinCriteria.onCriterion.toSql());
                }
            }
        }

        if(!ObjectUtils.isEmpty(criterionList)) {
            sb.append(WHERE);
            criterionList.forEach(Criterion::init);
            String where = criterionList.stream().map(Criterion::toSql).collect(Collectors.joining(AND));
            sb.append(where);
        }

        if(!ObjectUtils.isEmpty(orderList)) {
            sb.append(ORDER_BY);
            orderList.forEach(Order::init);
            String order = orderList.stream().map(Order::toSql).collect(Collectors.joining(COMMA));
            sb.append(order);
        }

        //追加限制信息
        if(page != null) {
            sb.append(LIMIT).append(S).append(QUEST).append(COMMA).append(QUEST);
        }

        return sb.toString();
    }

    public List<ParamValue> fetchParams() {
        //仅支持从root进行处理
        Criteria<T> root = rootCriteria();
        if(root != this) {
            return root.fetchParams();
        }

        List<ParamValue> paramValueList = Lists.newArrayList();

        if(!ObjectUtils.isEmpty(joinList)) {
            joinList.stream().map(Criteria::fetchParams)
                    .forEach(paramValueList::addAll);
        }

        criterionList.stream().map(Criterion::fetchParams)
                .forEach(paramValueList::addAll);

        //追加限制
        if(page != null) {
            paramValueList.add(new ParamValue<>(JdbcType.INTEGER, Integer.class, page.getOffset()));
            paramValueList.add(new ParamValue<>(JdbcType.INTEGER, Integer.class, page.getLimit()));
        }

        return paramValueList;
    }

    @Override
    public Criteria<T> clone() {
        try{
            @SuppressWarnings("unchecked")
            Criteria<T> other = (Criteria) super.clone();

            cloneProperty(other);

            return other;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void cloneProperty(Criteria<T> other) {
        //条件
        if(criterionList != null) {
            other.criterionList = criterionList.stream().map(t -> {
                //需要重新绑定至新的查询源
                val criterion = t.clone();
                criterion.injectCriteria(other);
                return criterion;
            }).collect(Collectors.toList());
        }

        //属性
        if(propertyList != null) {
            other.propertyList = propertyList.stream().map(t -> {
                //需要重新绑定
                val property = t.clone();
                property.injectCriteria(other);
                return property;
            }).collect(Collectors.toList());
        }

        //排序
        if(propertyList != null) {
            other.orderList = orderList.stream().map(t -> {
                //需要重新绑定
                val order = t.clone();
                order.injectCriteria(other);
                return order;
            }).collect(Collectors.toList());
        }

        //关联
        if(joinList != null) {
            other.joinList = joinList.stream().map(t -> {
                //需要重新绑定
                val joinProperty = t.clone();
                joinProperty.left = other;
                return joinProperty;
            }).collect(Collectors.toList());
        }
    }

    public static abstract class AbstractJoinCriteria<T2 extends Entity> extends Criteria<T2> {
        /** 表示关联的左边部分 */
        protected Criteria left;
        /** 关联类型 */
        protected JoinTypeValue joinType;
        /** 关联时使用的on条件 */
        protected PropertyCriterion onCriterion;

        public AbstractJoinCriteria(Criteria left, Class<T2> clazz, String alias, JoinTypeValue joinType, PropertyCriterion onCriterion) {
            super(clazz, alias, null, null, null);

            this.left = left;
            this.joinType = joinType;
            this.onCriterion = onCriterion;
            onCriterion.injectCriteria(left);
            onCriterion.injectOtherCriteria(this);
        }

        /**
         * 生成当前右边 join right 部分的sql语句
         *
         * @return join右侧部分的sql语句
         */
        protected abstract String toJoinRightSql();

        protected void reBindLeft(Criteria left) {
            this.left = left;
            this.onCriterion.injectCriteria(left);
        }

        @SuppressWarnings("unchecked")
        @Override
        public AbstractJoinCriteria<T2> clone() {
            return (AbstractJoinCriteria) super.clone();
        }

        @Override
        protected void cloneProperty(Criteria<T2> other) {
            //因为相应的条件均由root来处理,因此当前并不clone相应的属性信息
            AbstractJoinCriteria joinCriteria = (AbstractJoinCriteria) other;

            //重新绑定on条件
            val onCriterionOther = onCriterion.clone();
            onCriterionOther.injectOtherCriteria(joinCriteria);
            joinCriteria.onCriterion = onCriterionOther;
        }
    }

    /** 用于描述一个使用对象间关联的查询对象 */
    private static class EntityJoinCriteria<T2 extends Entity> extends AbstractJoinCriteria<T2> {
        public EntityJoinCriteria(Criteria<?> left, Class<T2> clazz, String alias, JoinTypeValue joinType, PropertyCriterion onCriterion) {
            super(left, clazz, alias, joinType, onCriterion);
        }

        @Override
        public String toJoinRightSql() {
            val entityInfo = EntityInfoHolder.get(getClazz());
            assert entityInfo != null;

            return entityInfo.getTable().getTableName();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Criteria rootCriteria() {
            return left.rootCriteria();
        }

        /** 因为相应的条件均为left来存储,因此当前对象没有条件信息 */
        @Override
        public List<ParamValue> fetchParams() {
            return ImmutableList.of();
        }
    }

    /** 用于描述一个使用独立查询进行关联的关联对象 */
    private static class CriteriaJoinCriteria<T2 extends Entity> extends AbstractJoinCriteria<T2> {
        private Criteria<T2> criteria;

        public CriteriaJoinCriteria(Criteria left, Criteria<T2> criteria, String alias, JoinTypeValue joinType, PropertyCriterion onCriterion) {
            super(left, null, alias, joinType, onCriterion);

            this.criteria = criteria;
        }

        @Override
        public String toJoinRightSql() {
            return "(" + criteria.toSql() + ")";
        }

        @Override
        public Criteria<T2> rootCriteria() {
            return criteria.rootCriteria();
        }

        /** 此为独立对象,因此具有相应的条件信息 */
        @Override
        public List<ParamValue> fetchParams() {
            return criteria.fetchParams();
        }

        /** 因为这里的内表联接,因此没有相应的模型表信息 */
        @Override
        public EntityInfo getEntityInfo() {
            return null;
        }
    }

    /** 用于描述一个子查询对象 */
    public static class SubCriteria<T3 extends Entity> extends Criteria<T3> {
        /** 外层查询对象 */
        private Criteria parent;

        private SubCriteria(Class<T3> clazz, String alias, Criteria parent) {
            super(clazz, alias, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());
            this.parent = parent;
            this.joinList = Lists.newArrayList();
        }

        @Override
        protected Optional<Criteria> findOptinalCriteria(String alias) {
            val value = super.findOptinalCriteria(alias);

            return value.isPresent() ? value : parent.findOptinalCriteria(alias);
        }
    }
}