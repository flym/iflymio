package io.iflym.mybatis.mapperx.util;

import com.google.common.base.Throwables;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.mapperx.Mapper;
import lombok.val;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.SqlSession;
import org.springframework.core.ResolvableType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

/**
 * 提供对mapper类的工具处理
 * 主要为访问 {@link MapperProxy}中的各项属性,因为一个mapper接口实际上的代理类即由 {@link MapperProxy} 来完成,但其中的一些属性并没有
 * 对外提供,这里即通过反射来获取相应的数据,以提供给内部实现时直接使用相应的属性来进行相应的访问处理
 * <p>
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
public class MapperUtils {

    private static MethodHandle sqlSessionLookup;
    private static MethodHandle mapperInterfaceLookup;

    static {
        try{
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class, int.class);
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            MethodHandles.Lookup lookup = constructor
                    .newInstance(MapperProxy.class, MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                            | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC);

            sqlSessionLookup = lookup.findGetter(MapperProxy.class, "sqlSession", SqlSession.class);
            mapperInterfaceLookup = lookup.findGetter(MapperProxy.class, "mapperInterface", Class.class);
        } catch(Throwable e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static SqlSession getSqlSession(Mapper mapper) {
        MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(mapper);

        try{
            return (SqlSession) sqlSessionLookup.invokeExact(mapperProxy);
        } catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mapper> Class<T> getMapperClass(Mapper mapper) {
        MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(mapper);

        try{
            return (Class<T>) mapperInterfaceLookup.invokeExact(mapperProxy);
        } catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable);
        }
    }

    /** 提供对mapper类中的泛型处理类的读取 */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> Class<T> getEntityType(Mapper mapper) {
        val mapperClass = getMapperClass(mapper);
        return getEntityType(mapperClass);
    }

    /** 提供对mapper类中的泛型处理类的读取 */
    @SuppressWarnings("unchecked")
    public static <T extends Entity, M extends Mapper<T>> Class<T> getEntityType(Class<M> mapperClass) {
        val resolvableType = ResolvableType.forClass(mapperClass).as(Mapper.class);
        if(resolvableType == ResolvableType.NONE) {
            return null;
        }
        val types = resolvableType.getGenerics();
        if(types.length == 0) {
            return null;
        }

        return (Class) types[0].resolve();
    }

}
