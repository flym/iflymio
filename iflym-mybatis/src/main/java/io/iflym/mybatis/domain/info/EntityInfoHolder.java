package io.iflym.mybatis.domain.info;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 持有所有的域模型信息
 * Created by flym on 2017/8/29.
 *
 * @author flym
 */
public class EntityInfoHolder {
    private static Map<Class, EntityInfo> entityMap = Maps.newIdentityHashMap();

    public static void register(EntityInfo entityInfo) {
        entityMap.put(entityInfo.getClazz(), entityInfo);
    }

    public static EntityInfo<?> get(Class clazz) {
        return entityMap.get(clazz);
    }

    /** 此类是否已注册 */
    public static boolean registered(Class clazz) {
        return entityMap.containsKey(clazz);
    }
}
