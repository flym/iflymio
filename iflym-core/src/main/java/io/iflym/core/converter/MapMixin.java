package io.iflym.core.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过一个额外的java类或接口来描述对象间转换关系,
 * 适用于源类或目标类不能源码调整的场景
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapMixin {
}
