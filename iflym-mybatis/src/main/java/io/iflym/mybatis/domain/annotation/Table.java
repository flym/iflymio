package io.iflym.mybatis.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对一个实体对象的数据表信息进行描述
 * Created by flym on 2017/8/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /** 表名 */
    String name() default "";
}
