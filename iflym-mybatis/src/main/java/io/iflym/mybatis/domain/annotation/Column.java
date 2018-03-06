package io.iflym.mybatis.domain.annotation;

import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对一个列信息的描述
 * Created by flym on 2017/8/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /** 列名 */
    String name() default "";

    /** 列注释信息,即对一个列的可视化描述 */
    String comment() default "";

    /** 列类型 */
    JdbcType type() default JdbcType.NULL;
}
