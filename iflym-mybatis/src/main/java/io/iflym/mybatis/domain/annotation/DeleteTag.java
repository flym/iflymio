package io.iflym.mybatis.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记失效状态列
 *
 * @author luyi
 * @date 2017/11/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DeleteTag {
    /**
     * 无效状态值
     */
    String value() default "0";
}
