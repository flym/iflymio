package io.iflym.mybatis.domain.annotation;

import io.iflym.mybatis.domain.KeyGenerator;
import io.iflym.mybatis.domain.UniqueKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luyi
 * @date 2017/12/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UniqueId {
    /**
     * 此惟一索引的组名
     * 一个实体可以有多个惟一key组
     */
    String group() default UniqueKey.GROUP_DEFAULT;

    /**
     * 同一个组内多个列时相应的序号值
     * 如果没有指定,则出现多个组时,以实体定义的字段顺序为准
     */
    int order() default -1;

    /**
     * 如果当前列为null,则在保存时默认的值生成器,以生成相应的默认值
     * 此处设置default值，只是标识keyGenerator为非必须属性， 无实质意义
     */
    Class<? extends KeyGenerator> keyGenerator() default KeyGenerator.class;

}
