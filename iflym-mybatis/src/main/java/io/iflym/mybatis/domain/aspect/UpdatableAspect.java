package io.iflym.mybatis.domain.aspect;

import io.iflym.core.util.ObjectUtils;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Updatable;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.domain.info.EntityInfo;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.domain.util.UpdateUtils;
import io.iflym.mybatis.exception.MybatisException;
import lombok.experimental.var;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author flym
 * Created by flym on 2017/9/1.
 */
@Aspect
public class UpdatableAspect {
    @Pointcut("execution(public void io.iflym.mybatis.domain.Updatable+.set*(*))")
    private static void invokeSetter() {
    }

    @SuppressWarnings({"AroundAdviceStyleInspection", "ArgNamesWarningsInspection"})
    @Around(value = "invokeSetter() && this(self)")
    public void beforeSetter(ProceedingJoinPoint joinPoint, Updatable self) throws Throwable {
        //标记判断
        if(!UpdateUtils.marked(self)) {
            joinPoint.proceed();
            return;
        }

        //对象类型判断
        if(!(self instanceof Entity)) {
            joinPoint.proceed();
            return;
        }

        //非空判断
        if(UpdateUtils.currentNonEmptySet()) {
            Object p = joinPoint.getArgs()[0];
            if(ObjectUtils.isEmpty(p)) {
                return;
            }
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = self.getClass();

        val columnInfo = getColumn(clazz, EntityInfoHolder.get(clazz), methodSignature);
        Field property = columnInfo.getField();
        ReflectionUtils.makeAccessible(property);

        Object currentOldValue = property.get(self);
        Object currentNewValue = joinPoint.getArgs()[0];
        if(!Objects.equals(currentOldValue, currentNewValue)) {
            UpdateUtils.putUpdateItem(self, property, currentOldValue, currentNewValue);
        }

        joinPoint.proceed();
    }

    private <T extends Entity> ColumnInfo getColumn(Class<?> clazz, EntityInfo<T> entityInfo, MethodSignature signature) {
        String name = signature.getParameterNames()[0];
        var column = entityInfo.getColumn(name);
        if(column != null) {
            return column;
        }

        val methodName = signature.getName();
        val methodNamePostfix = methodName.substring(3);

        //没拿到，其可能是kotlin类，也可能是编译上的问题，导致没有拿到名字信息，这里重新从列信息中查找路由
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        return columnList.stream()
                .filter(t -> t.getPropertyName().equalsIgnoreCase(methodNamePostfix))
                .findFirst()
                .orElseThrow(() -> new MybatisException("不能通过方法找到相应的列名:" + methodName));
    }
}
