package io.iflym.core.converter;

import java.lang.annotation.*;

/**
 * 用于在字段上单独定义映射注解
 * created at 2019-04-16
 *
 * @author flym
 */
public interface MapField {
    /** 在目的类字段上配置与源类的映射关系 */
    @Repeatable(Froms.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface From {
        /** 源类名 */
        Class<?> clazz();

        /** 源类属性 */
        String property();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    @interface Froms {
        From[] value();
    }

    /** 在源类字段上配置与目的类的映射关系 */
    @Repeatable(Tos.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface To {
        /** 目的类名 */
        Class<?> clazz();

        /** 目的类属性 */
        String property();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    @interface Tos {
        To[] value();
    }
}
