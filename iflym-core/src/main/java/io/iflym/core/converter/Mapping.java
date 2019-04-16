package io.iflym.core.converter;

import java.lang.annotation.*;

/**
 * 标准的转换映射定义，放在类上，以用于描述统一的源，目的信息，以及统一的字段转换规则
 */
@Repeatable(Mapping.Mappings.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mapping {

    /**
     * 转换源类,当注解在源类时,此值被忽略
     * 注: from to中必定其中有1个有值。当注解在Mixin类上时，两个值都需要存在
     */
    Class<?> from() default Class.class;

    /**
     * 转换目的类,当注解在目的类时，此值被忽略
     * 注: from to中必定其中有1个有值。当注解在Mixin类上时，两个值都需要存在
     */
    Class<?> to() default Class.class;

    /**
     * 统一的字段间转换
     * 此转换为附加关系，当有此配置时优先使用此配置，否则使用同名转换原则
     * 注：如果字段上同样配置了转换，则以字段上的为准
     */
    Field[] fields() default {};

    @interface Field {
        /**
         * 源属性名,当注解在源类字段上时，此值被忽略
         * 注：from to 必须其中有1个有值。当注解在Mixin类上时，两个值都需要存在
         */
        String from();

        /**
         * 目的属性名,当注解在目的类字段上时，此值被忽略
         * 注：from to 必须其中有1个有值。当注解在Mixin类上时，两个值都需要存在
         */
        String to();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Mappings {
        Mapping[] value();
    }
}
