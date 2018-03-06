package io.iflym.core.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author flym
 * Created by flym on 2017/8/30.
 */
public class ObjectUtils {
    /**
     * 用于判断一个对象是否为空的
     *
     * @see org.springframework.util.ObjectUtils
     */
    public static boolean isEmpty(Object obj) {
        //null对象
        if(obj == null) {
            return true;
        }

        //序列串
        if(obj instanceof CharSequence) {
            CharSequence cs = (CharSequence) obj;
            for(int i = 0; i < cs.length(); i++) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        //数组
        if(obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if(length == 0) {
                return true;
            }

            for(int i = 0; i < length; i++) {
                Object object = Array.get(obj, i);
                if(!isEmpty(object)) {
                    return false;
                }
            }
        }

        //集合
        if(obj instanceof Collection) {
            Collection<?> c = (Collection) obj;
            if(c.isEmpty()) {
                return true;
            }

            return c.stream().allMatch(ObjectUtils::isEmpty);
        }

        //映射
        if(obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }
}
