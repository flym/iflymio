package io.iflym.core.converter.generator;

import com.google.common.collect.Sets;
import io.iflym.core.converter.MapField;
import io.iflym.core.converter.Mapping;
import io.iflym.core.converter.generator.internal.BeanUtils;
import io.iflym.core.converter.generator.internal.ReflectionUtils;
import io.iflym.core.util.ListUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于描述具体的每个类的属性的映射信息
 * created at 2019-04-16
 *
 * @author flym
 */
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@ToString(of = "name")
@EqualsAndHashCode(of = "name")
public class PropertyInfo {
    /** 自己这边的描述符 */
    PropertyDescriptor descriptor;

    /** 自己这边的字段(如果有) */
    Field field;

    /**
     * 自己这边的方法(必须有)
     * 当当前属性对象表示源时，此方法为getter；当表示目的时，此方法为setter
     */
    Method method;

    /** 自己这边的属性名 */
    @Getter
    String name;

    /**
     * 显示设置的期望匹配到对方的属性名,如果没有显示配置，这里的长度为0
     * 备注：如果当前为 to 方，则这里的reverseNameList长度不能>1,因为一个dest不能从多个source来
     */
    @Getter
    Set<String> priorityReverseNameSet = Sets.newHashSet();

    //---------------------------- 私有方法 start ------------------------------//

    private static void fillInfo(PropertyInfo info, Class clazz, PropertyDescriptor descriptor) {
        info.descriptor = descriptor;
        val name = descriptor.getName();
        info.name = name;
        info.field = ReflectionUtils.findField(clazz, name);
    }

    private static PropertyInfo firstOrNull(List<PropertyInfo> list) {
        if(list.size() > 1) {
            val result = list.get(0);
            log.warn("找到多个匹配的数据,将使用第1个。匹配值:{},决定值:{}", list, result);
            return result;
        }

        return list.isEmpty() ? null : list.get(0);
    }

    private static List<MapField.To> filterClass4To(MapField.To[] tos, Class to) {
        return ListUtils.filter(Arrays.asList(tos), t -> Objects.equals(t.clazz(), to));
    }

    private static List<MapField.From> filterClass4From(MapField.From[] froms, Class from) {
        return ListUtils.filter(Arrays.asList(froms), t -> Objects.equals(t.clazz(), from));
    }

    private static PropertyInfo findOrNull(List<PropertyInfo> list, String name) {
        return ListUtils.findFirst(list, t -> Objects.equals(t.name, name));
    }

    private static void fillTypeMapping4Getter(List<PropertyInfo> list, Class<?> from, Class<?> to) {
        val mapping = ListUtils.findFirstOptional(Arrays.asList(from.getAnnotationsByType(Mapping.class)), t -> Objects.equals(t.to(), to));
        mapping.ifPresent(t -> fillMappingForGetter(list, from, to, t));
    }

    private static void fillTypeMapping4Setter(List<PropertyInfo> list, Class<?> to, Class<?> from) {
        val mapping = ListUtils.findFirstOptional(Arrays.asList(to.getAnnotationsByType(Mapping.class)), t -> Objects.equals(t.from(), from));
        mapping.ifPresent(t -> fillMappingForSetter(list, to, from, t));
    }

    private static void fillReverseName4Setter(PropertyInfo info, Class from) {
        val methodTos = filterClass4From(info.method.getAnnotationsByType(MapField.From.class), from);
        val fieldTos = info.field == null ? Collections.<MapField.From>emptyList() : filterClass4From(info.field.getAnnotationsByType(MapField.From.class), from);

        if(!methodTos.isEmpty() || !fieldTos.isEmpty()) {
            val nameSet = Sets.<String>newHashSet();
            methodTos.stream().map(MapField.From::property).forEach(nameSet::add);
            fieldTos.stream().map(MapField.From::property).forEach(nameSet::add);

            //因为对于目的来说，来源必须只能有1个，因此这里提前处理掉
            if(nameSet.size() > 1) {
                val finalName = nameSet.iterator().next();
                log.warn("属性:{}存在多个来源，将使用first值(任意的). 获取值:{}, 决定值:{}", info.name, nameSet, finalName);

                nameSet.clear();
                nameSet.add(finalName);
            }
            info.priorityReverseNameSet = nameSet;
        }
    }

    private static void fillReverseName4Getter(PropertyInfo info, Class to) {
        val methodTos = filterClass4To(info.method.getAnnotationsByType(MapField.To.class), to);
        val fieldTos = info.field == null ? Collections.<MapField.To>emptyList() : filterClass4To(info.field.getAnnotationsByType(MapField.To.class), to);

        if(!methodTos.isEmpty() || !fieldTos.isEmpty()) {
            val nameSet = Sets.<String>newHashSet();
            methodTos.stream().map(MapField.To::property).forEach(nameSet::add);
            fieldTos.stream().map(MapField.To::property).forEach(nameSet::add);
            info.priorityReverseNameSet = nameSet;
        }
    }

    private static PropertyInfo buildFromGetter(Class from, PropertyDescriptor descriptor, Class to) {
        //跳过特殊的getClass
        if(descriptor.getName().equals("class") || descriptor.getReadMethod() == null) {
            return null;
        }

        val info = new PropertyInfo();
        fillInfo(info, from, descriptor);

        info.method = descriptor.getReadMethod();

        fillReverseName4Getter(info, to);

        return info;
    }

    private static PropertyInfo buildFromSetter(Class to, PropertyDescriptor descriptor, Class from) {
        if(descriptor.getWriteMethod() == null) {
            return null;
        }

        val info = new PropertyInfo();
        fillInfo(info, to, descriptor);

        info.method = descriptor.getWriteMethod();

        fillReverseName4Setter(info, from);

        return info;
    }

    //---------------------------- 私有方法 end ------------------------------//

    public static PropertyInfo findFrom(List<PropertyInfo> fromList, String expectName, boolean mustFound) {
        //优先找显示设置的
        var value = firstOrNull(ListUtils.filter(fromList, t -> t.priorityReverseNameSet.contains(expectName)));
        if(value != null) {
            return value;
        }

        //在默认匹配的，因为默认匹配的值即与name相同,因此可以理解为即找两边name相同的匹配
        value = firstOrNull(ListUtils.filter(fromList, t -> Objects.equals(t.name, expectName)));
        if(value != null) {
            return value;
        }

        //因为这里即to方要求必须匹配到，因此这里直接报错
        if(mustFound) {
            throw new RuntimeException("并没有找到期望映射名为:" + expectName + "的映射");
        }

        return null;
    }

    public static void fillMappingForGetter(List<PropertyInfo> list, Class from, Class to, Mapping mapping) {
        for(val f : mapping.fields()) {
            val info = findOrNull(list, f.from());
            if(info != null) {
                info.priorityReverseNameSet.add(f.to());
            }
        }
    }

    public static void fillMappingForSetter(List<PropertyInfo> list, Class to, Class from, Mapping mapping) {
        for(val f : mapping.fields()) {
            val info = findOrNull(list, f.to());
            //如果setter中本身就有，则以属性上的为准
            if(info != null && info.priorityReverseNameSet.isEmpty()) {
                info.priorityReverseNameSet.add(f.from());
            }
        }
    }

    public static List<PropertyInfo> buildListFromGetter(Class clazz, Class to) {
        val descriptors = BeanUtils.getPropertyDescriptors(clazz);
        val list = Stream.of(descriptors).map(t -> buildFromGetter(clazz, t, to)).filter(Objects::nonNull).collect(Collectors.toList());
        //补充type上的映射信息
        fillTypeMapping4Getter(list, clazz, to);

        return list;
    }

    public static List<PropertyInfo> buildListFromSetter(Class clazz, Class from) {
        val descriptors = BeanUtils.getPropertyDescriptors(clazz);
        val list = Stream.of(descriptors).map(t -> buildFromSetter(clazz, t, from)).filter(Objects::nonNull).collect(Collectors.toList());

        //补充type上的映射信息
        fillTypeMapping4Setter(list, clazz, from);

        return list;
    }

    public String getterName() {
        return method.getName();
    }

    public String setterName() {
        return method.getName();
    }
}
