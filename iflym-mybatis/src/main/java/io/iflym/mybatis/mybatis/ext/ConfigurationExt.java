package io.iflym.mybatis.mybatis.ext;

import io.iflym.core.util.MethodUtils;
import lombok.val;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于扩展默认的configuration, 以提供一些额外的功能,以方便进行数据处理
 *
 * @author flym
 * Created by flym on 2017/10/25.
 */
public class ConfigurationExt extends Configuration {

    //---------------------------- 替换statement处理 start ------------------------------//

    protected Map<String, MappedStatement> mutableStatementMap = new StrictMapExt<>("Mapped Statements collection");

    @Override
    public void addMappedStatement(MappedStatement ms) {
        mutableStatementMap.put(ms.getId(), ms);
    }

    @Override
    public Collection<String> getMappedStatementNames() {
        buildAllStatements();
        return mutableStatementMap.keySet();
    }

    @Override
    public Collection<MappedStatement> getMappedStatements() {
        buildAllStatements();
        return mutableStatementMap.values();
    }

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        if(validateIncompleteStatements) {
            buildAllStatements();
        }
        return mutableStatementMap.get(id);
    }

    @Override
    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        if(validateIncompleteStatements) {
            buildAllStatements();
        }
        return mutableStatementMap.containsKey(statementName);
    }

    //---------------------------- 替换statement处理 end ------------------------------//

    protected static class StrictMapExt<V> extends StrictMap<V> {
        private static final Method PUT_HASH_MAP = ReflectionUtils.findMethod(HashMap.class, "put", Object.class, Object.class);
        private static final Method GET_HASH_MAP = ReflectionUtils.findMethod(HashMap.class, "get", Object.class);

        public StrictMapExt(String name) {
            super(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V put(String key, V value) {
            val dotIndex = key.lastIndexOf('.');
            if(dotIndex != -1) {
                final String shortKey = getLastName(key);
                if(MethodUtils.invokeSpecial(GET_HASH_MAP, this, shortKey) == null) {
                    MethodUtils.invokeSpecial(PUT_HASH_MAP, this, shortKey, value);
                } else {
                    MethodUtils.invokeSpecial(PUT_HASH_MAP, this, shortKey, new Ambiguity(shortKey));
                }
            }

            return MethodUtils.invokeSpecial(PUT_HASH_MAP, this, key, value);
        }
    }

    private static String getLastName(String key) {
        int lastIndex = key.lastIndexOf('.');
        return lastIndex == -1 ? key : key.substring(lastIndex + 1);
    }
}
