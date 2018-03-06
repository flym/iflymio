package io.iflym.mybatis.domain.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import io.iflym.mybatis.domain.Attribute;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 用于提供对attr属性的数据存储处理
 * 因为attribute为一个接口,自身不具备提供属性的能力,因此使用额外的容器来进行相应的数据存储
 * @author flym
 * <p>
 * Created by flym on 2017/8/29.
 */
public class AttrUtils {

    /**
     * 内部用于实际进行数据存储的容器
     * 采用weakKeys以支持快速回收不再使用的对象
     */
    private static Cache<Attribute, Map<String, Object>> cache = CacheBuilder.newBuilder().weakKeys().build();

    /** 获取属性对象相对应的属性信息. 如果没有对应的属性,则返回null */
    public static Map<String, Object> attr(Attribute attribute) {
        return cache.getIfPresent(attribute);
    }

    /**
     * 为属性对象设置单个属性以及相应的值
     * 如果提供的 value 为 null,则忽略
     */
    public static <V_> void attr(Attribute attribute, String key, V_ value) {
        if(value == null) {
            return;
        }

        try{
            cache.get(attribute, Maps::newHashMap).put(key, value);
        } catch(ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
