package io.iflym.mybatis.mapperx;

import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为自动生成的sql提供一个注解暗示,以表示生成sql需要提供哪些额外的信息
 * Created by flym on 2017/8/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sql {
    /** 当前方法的处理类型,即sql的命令类型 */
    SqlCommandType type();
}
