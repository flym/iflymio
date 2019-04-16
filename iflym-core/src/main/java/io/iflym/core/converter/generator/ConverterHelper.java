package io.iflym.core.converter.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.iflym.core.converter.MapMixin;
import io.iflym.core.converter.Mapping;
import io.iflym.core.tuple.Tuple3;
import io.iflym.core.util.ExceptionUtils;
import io.iflym.core.util.converter.Converter;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 辅助器,辅助完成converter对象的生成
 * <p>
 * 注：此生成需要借助javassist的能力，使用时需要将此jar引入到项目中
 * created at 2019-04-16
 *
 * @author flym
 */
@Slf4j
public class ConverterHelper {
    private static final AtomicLong NUM_POSTFIX = new AtomicLong(1);

    private static ClassPool currentClassPool;

    static {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new ClassClassPath(Converter.class));
        currentClassPool = classPool;
    }

    //---------------------------- 私有方法 start ------------------------------//

    /** 从两个类中分别获取相应的生成映射关系 */
    private static Map<PropertyInfo, PropertyInfo> generatePair(Class to, Class from, Mapping extraMapping) {
        val mappingOptional = Optional.ofNullable(extraMapping);

        val setterList = PropertyInfo.buildListFromSetter(to, from);
        mappingOptional.ifPresent(t -> PropertyInfo.fillMappingForSetter(setterList, to, from, t));

        val getterList = PropertyInfo.buildListFromGetter(from, to);
        mappingOptional.ifPresent(t -> PropertyInfo.fillMappingForGetter(getterList, from, to, t));
        //匹配规则
        //to 中的 revername从froms中找，如果为null并且不是default，则报错，否则定位到froms中支持revernames为当前值的列表,如果存在多个值，则取第1个, 还没找到，则忽略

        val map = Maps.<PropertyInfo, PropertyInfo>newHashMap();

        setterList.forEach(d -> {
            //dest的reverName限制为1个
            val mustFound = !d.getPriorityReverseNameSet().isEmpty();
            val dreverseName = d.getPriorityReverseNameSet().isEmpty() ? d.getName() : d.getPriorityReverseNameSet().iterator().next();
            val s = PropertyInfo.findFrom(getterList, dreverseName, mustFound);
            if(s != null) {
                map.put(d, s);
            }
        });

        return map;
    }

    private static <S, D> LazyConverter<S, D> generateLazyConverter(Class<S> from, Class<D> to, Mapping extraMapping) {
        return LazyConverter.build(() -> ExceptionUtils.doFunLogAndRethrowE(log, () -> doGenerateConverter(from, to, extraMapping)));
    }

    @SuppressWarnings("unchecked")
    private static <S, D> Converter<S, D> doGenerateConverter(Class<S> from, Class<D> to, Mapping extraMapping) throws Exception {
        val interfaceClazz = Converter.class;

        val newName = "io.iflym.core.util.converter." + from.getSimpleName() + "2" + to.getSimpleName() + "Converter" + NUM_POSTFIX.getAndIncrement();

        val ctClass = currentClassPool.makeClass(newName);

        //---------------------------- 实现接口 start ------------------------------//

        CtClass interfaceClass = currentClassPool.getCtClass(interfaceClazz.getName());

        val fromTypeName = from.getName();
        val toTypeName = to.getName();

        //处理签名信息
        val fromSig = new SignatureAttribute.ClassType(fromTypeName);
        val toSig = new SignatureAttribute.ClassType(toTypeName);
        val typeArg = new SignatureAttribute.TypeArgument(fromSig);
        val typeArg2 = new SignatureAttribute.TypeArgument(toSig);
        val interfaceClassType = new SignatureAttribute.ClassType(interfaceClass.getName(), new SignatureAttribute.TypeArgument[]{typeArg, typeArg2});

        val signature = new SignatureAttribute.ClassSignature(null, null, new SignatureAttribute.ClassType[]{interfaceClassType});

        ctClass.setGenericSignature(signature.encode());
        ctClass.addInterface(interfaceClass);

        //---------------------------- 实现接口 end ------------------------------//

        //---------------------------- 实现方法 start ------------------------------//

        //拼方法实现字符串

        val fromVarName = "from";
        val toVarName = "to";
        StringBuilder builder = new StringBuilder("{");

        //val input = (Input)s;
        builder.append(fromTypeName).append(" ").append(fromVarName).append(" = (").append(fromTypeName).append(")$1;");

        //val output = new outputType();
        builder.append(toTypeName).append(" ").append(toVarName).append(" = new ").append(toTypeName).append("();");

        val pairMap = generatePair(to, from, extraMapping);
        pairMap.forEach((out, input) -> {
            //output.setxxx(input.getyyy());
            builder.append(toVarName).append(".").append(out.setterName()).append("(").append(fromVarName).append(".").append(input.getterName()).append("());");
        });

        //return xxx;
        builder.append("return ").append(toVarName).append(";");

        builder.append("}");

        val body = builder.toString();
        log.debug("generate converter.from:{}, to:{}, body:{}", body);

        val objCtClass = currentClassPool.getCtClass("java.lang.Object");

        val method = CtNewMethod.make(objCtClass, "apply", new CtClass[]{objCtClass}, new CtClass[0], null, ctClass);
        method.setBody(body);

        val methodSignature = new SignatureAttribute.MethodSignature(null, new SignatureAttribute.Type[]{fromSig}, toSig, null);

        method.setGenericSignature(methodSignature.encode());
        ctClass.addMethod(method);

        //---------------------------- 实现方法 end ------------------------------//

        val clazz = ctClass.toClass(Thread.currentThread().getContextClassLoader(), ConverterHelper.class.getProtectionDomain());

        return (Converter<S, D>) clazz.newInstance();
    }

    //---------------------------- 私有方法 end ------------------------------//

    public static <S, D> Converter<S, D> generateConverter(Class<S> from, Class<D> to) {
        return generateLazyConverter(from, to, null);
    }

    public static List<Tuple3<Class<?>, Class<?>, Converter>> generateConverterUseMix(Class<?> mixClass) {
        if(!mixClass.isAnnotationPresent(MapMixin.class)) {
            throw new RuntimeException("mix类:" + mixClass + "上并没有相应的mapMixin注解");
        }

        val mappings = mixClass.getAnnotationsByType(Mapping.class);
        val converterList = Lists.<Tuple3<Class<?>, Class<?>, Converter>>newArrayList();

        for(val mapping : mappings) {
            //mixin中from 和 to 均不能为null
            val from = mapping.from();
            val to = mapping.to();
            if(from == Class.class || to == Class.class) {
                throw new RuntimeException("在mix中 from 和 to必须填写.当前mix类:" + mixClass);
            }
            converterList.add(new Tuple3<>(from, to, generateLazyConverter(from, to, mapping)));
        }

        return converterList;
    }
}
