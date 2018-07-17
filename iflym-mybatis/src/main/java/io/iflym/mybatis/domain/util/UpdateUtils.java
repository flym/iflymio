package io.iflym.mybatis.domain.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.iflym.mybatis.domain.Updatable;
import io.iflym.mybatis.domain.UpdateItem;
import lombok.val;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于辅助完成对可更新对象的处理
 * Created by flym on 2017/9/1.
 *
 * @author flym
 */
public class UpdateUtils {
    /** 内部用于持有相应的可更新对象的各个实现状态 */
    private static Cache<Updatable, Map<Field, UpdateItem>> updatableCache = CacheBuilder.newBuilder().weakKeys().build();

    /** 对对象进行标记 */
    public static void markUpdate(Updatable updatable) {
        //这里使用linkedHashMap,以保证按调用顺序排序,以让调用方在返回记录时能够按刚才的顺序返回数据
        updatableCache.put(updatable, Maps.newLinkedHashMap());
    }

    /** 取消对对象的标记 */
    public static void unmarkUpdate(Updatable updatable) {
        updatableCache.invalidate(updatable);
    }

    /** 判断相应的对象是否已被标记 */
    public static boolean marked(Updatable updatable) {
        return updatableCache.getIfPresent(updatable) != null;
    }

    /** 追加属性更新条目 */
    public static <V> void putUpdateItem(Updatable updatable, Field property, V oldValue, V newValue) {
        Optional.ofNullable(updatableCache.getIfPresent(updatable))
                .ifPresent(m -> m.put(property, new UpdateItem<>(updatable, property, oldValue, newValue)));
    }

    /** 获取相应的属性更新条目(如果没有更新条目,则返回emptyList,不可修改) */
    public static List<UpdateItem> getUpdateItemList(Updatable updatable) {
        return Optional.ofNullable(updatableCache.getIfPresent(updatable))
                .map(t -> ImmutableList.copyOf(t.values()))
                .orElse(ImmutableList.of());
    }

    /**
     * 在执行update及set方法时，增加对参数的判断，仅将为 nonEmpty的参数进行set调用
     *
     * @see io.iflym.core.util.ObjectUtils#isEmpty(Object)
     */
    public static void nonEmptyUpdate(Runnable run) {
        val holder = NonEmptyFlagHolder.INSTANCE;
        try{
            holder.incrementFlag();
            run.run();
        } finally {
            holder.decrementFlag();
        }
    }

    /** 当前是否启用对nonEmpty的判断 */
    public static boolean currentNonEmptySet() {
        return NonEmptyFlagHolder.INSTANCE.requireNonEmpty();
    }

    private static class NonEmptyFlagHolder {
        private static final ThreadLocal<AtomicInteger> f = ThreadLocal.withInitial(AtomicInteger::new);

        private static final NonEmptyFlagHolder INSTANCE = new NonEmptyFlagHolder();

        void incrementFlag() {
            f.get().getAndIncrement();
        }

        void decrementFlag() {
            AtomicInteger atomicInteger = f.get();
            int i = atomicInteger.decrementAndGet();
            if(i <= 0) {
                f.remove();
            }
        }

        boolean requireNonEmpty() {
            return f.get().get() > 0;
        }
    }
}
