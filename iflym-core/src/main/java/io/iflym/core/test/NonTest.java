package io.iflym.core.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不需要进行单元测试注解
 * 仅用于表示一些构造函数或方法,本身逻辑非常简单,不需要进行单元测试.
 * created at 2018-04-02
 *
 * @author flym
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface NonTest {
}
