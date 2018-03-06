package io.iflym.mybatis.domain.aspect;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Updatable;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.domain.util.UpdateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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
        if(!UpdateUtils.marked(self)) {
            joinPoint.proceed();
            return;
        }

        if(!(self instanceof Entity)) {
            joinPoint.proceed();
            return;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class clazz = self.getClass();

        String fieldName = methodSignature.getParameterNames()[0];
        Field property = EntityInfoHolder.get(clazz).getColumn(fieldName).getField();
        ReflectionUtils.makeAccessible(property);

        Object currentOldValue = property.get(self);
        Object currentNewValue = joinPoint.getArgs()[0];
        if(!Objects.equals(currentOldValue, currentNewValue)) {
            UpdateUtils.putUpdateItem(self, property, currentOldValue, currentNewValue);
        }

        joinPoint.proceed();
    }
}
