package io.iflym.mybatis.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对一个主键列的主键信息描述
 * 注:一个列即使为主键,相应的 {@link Column} 注解也是需要的
 * 当前注解仅是对主键信息进行一个标识
 * Created by flym on 2017/8/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {

}
