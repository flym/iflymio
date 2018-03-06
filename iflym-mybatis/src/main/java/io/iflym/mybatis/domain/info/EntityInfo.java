package io.iflym.mybatis.domain.info;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.annotation.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.iflym.mybatis.domain.info.ColumnInfo.UNDEFINED;

/**
 * 对一个域模型的描述,每一个域模型均需要有相应的对象对其进行描述,以持有相应的结构信息
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
@Getter
public class EntityInfo<T extends Entity> {
    /**
     * 此模型所对应的类
     */
    private Class<T> clazz;

    /**
     * 相应的数据表信息
     */
    private TableInfo table;

    /**
     * 相应的列信息
     */
    @Getter(AccessLevel.NONE)
    private Map<Field, ColumnInfo> columnMap;

    /**
     * 为一个类构建出相应的模型对象
     */
    public static <T extends Entity> EntityInfo<T> build(Class<T> clazz) {
        val entityInfo = new EntityInfo<T>();
        entityInfo.clazz = clazz;
        entityInfo.table = TableInfo.build(clazz);

        val fieldList = allField(clazz);
        val columnMap = Maps.<Field, ColumnInfo>newLinkedHashMap();
        val handledSet = Sets.<String>newHashSet();
        val naturalOrder = new AtomicInteger();
        fieldList.forEach(f -> {
            val name = f.getName();
            if(handledSet.contains(name)) {
                return;
            }
            handledSet.add(name);

            val columnInfo = ColumnInfo.build(f, naturalOrder.getAndIncrement());
            columnMap.put(f, columnInfo);
        });
        entityInfo.columnMap = columnMap;

        return entityInfo;
    }

    /**
     * 返回所有字段,此返回字段按照继承体系, 优先级降低,即最末层的优先级最高
     * 对于使用者,如果下层类有字段覆盖上层,则在使用时应使用contains进行判断和处理
     */
    private static List<Field> allField(Class clazz) {
        List<Field> resultList = Lists.newArrayList();

        Class currentClass = clazz;
        while(currentClass != Object.class) {
            for(Field field : currentClass.getDeclaredFields()) {
                val anno = field.getAnnotation(Column.class);
                if(anno == null) {
                    continue;
                }
                resultList.add(field);
            }

            currentClass = currentClass.getSuperclass();
        }

        return resultList;
    }

    @Getter(AccessLevel.NONE)
    private transient List<ColumnInfo> columnList;

    /**
     * 获取此实体对象所有的列信息
     */
    public List<ColumnInfo> getColumnList() {
        if(columnList == null) {
            return columnList = Lists.newArrayList(columnMap.values());
        }

        return columnList;
    }

    @Getter(AccessLevel.NONE)
    private transient List<ColumnInfo> idColumnList;

    /**
     * 获取此实体对象的主键列
     */
    public List<ColumnInfo> getIdColumnList() {
        if(idColumnList == null) {
            idColumnList = getColumnList().stream().filter(ColumnInfo::isIdColumn).collect(Collectors.toList());
        }

        return idColumnList;
    }

    @Getter(AccessLevel.NONE)
    private transient Map<String, List<ColumnInfo>> uniqueKeyColumnMap;
    private static Comparator<ColumnInfo> uniqueKeyComparator = Comparator.<ColumnInfo>comparingInt(t -> t.getUniqueId().order())
            .thenComparingInt(ColumnInfo::getFieldNaturalOrder);

    /**
     * 获取此实体对象的UK列
     * 如果无, 则返回 emptyList
     */
    public List<ColumnInfo> getUniqueIdColumnList(String group) {
        if(uniqueKeyColumnMap == null) {
            uniqueKeyColumnMap = Maps.newHashMap();
            getColumnList().stream().filter(ColumnInfo::isUniqueIdColumn).forEach(t -> {
                val groupName = t.getUniqueId().group();
                uniqueKeyColumnMap.computeIfAbsent(groupName, any -> Lists.newArrayList()).add(t);
            });

            uniqueKeyColumnMap.values().forEach(t -> t.sort(uniqueKeyComparator));
        }

        return uniqueKeyColumnMap.getOrDefault(group, Collections.emptyList());
    }

    @Getter(AccessLevel.NONE)
    private transient ColumnInfo deleteTagColumn = UNDEFINED;

    /** 获取删除标记的列 */
    public ColumnInfo getDeleteTagColumn() {
        if(deleteTagColumn == UNDEFINED) {
            deleteTagColumn = getColumnList().stream().filter(ColumnInfo::isDeleteTagColumn).findFirst().orElse(null);
        }

        return deleteTagColumn;
    }


    /**
     * 获取此实体对象指定属性的列信息
     */
    public ColumnInfo getColumn(Field property) {
        return columnMap.get(property);
    }

    @Getter(AccessLevel.NONE)
    private transient Map<String, ColumnInfo> propertyNameColumnMap;

    public ColumnInfo getColumn(String property) {
        if(propertyNameColumnMap == null) {
            propertyNameColumnMap = Maps.newLinkedHashMap();
            columnMap.forEach((k, v) -> propertyNameColumnMap.put(k.getName(), v));
        }

        return propertyNameColumnMap.get(property);
    }
}
