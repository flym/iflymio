/* Created by flym at 2014/6/24 */
package io.iflym.core.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.primitives.*;
import io.iflym.core.util.converter.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 数据转换工具类，用于将指定的类转换为另一个类
 * <p>
 * 注: 此类的转换器,大部分从google guava 以及spring core中 参考而来,这里通过定义统一的转换器,以达到一个标准化运用的效果
 * <p>
 * 为简化相应的应用,此类的类型匹配为强匹配, 不支持类型继承等处理
 *
 * @author flym
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
public class ConvertUtils {
    private static final Map<Class, Map<Class, Converter>> CONVERT_MAP = Maps.newIdentityHashMap();

    static {
        //添加默认的实现

        //---------------------------- 基本类型互转 start ------------------------------//
        Converter identity = t -> t;
        //int类型
        addConverter(Integer.class, int.class, identity);
        addConverter(int.class, Integer.class, identity);

        //long类型
        addConverter(Long.class, long.class, identity);
        addConverter(long.class, Long.class, identity);

        //short
        addConverter(Short.class, short.class, identity);
        addConverter(short.class, Short.class, identity);

        //byte
        addConverter(Byte.class, byte.class, identity);
        addConverter(byte.class, Byte.class, identity);

        //boolean
        addConverter(Boolean.class, boolean.class, identity);
        addConverter(boolean.class, Boolean.class, identity);

        //char
        addConverter(Character.class, char.class, identity);
        addConverter(char.class, Character.class, identity);

        //float
        addConverter(Float.class, float.class, identity);
        addConverter(float.class, Float.class, identity);

        //double
        addConverter(Double.class, double.class, identity);
        addConverter(double.class, Double.class, identity);

        //---------------------------- 基本类型互转 end ------------------------------//

        //---------------------------- 数字与数字之间 start ------------------------------//
        //int

        //int2long
        addConverter(int.class, long.class, Integer2LongConverter.INSTANCE);
        addConverter(int.class, Long.class, Integer2LongConverter.INSTANCE);
        addConverter(Integer.class, long.class, Integer2LongConverter.INSTANCE);
        addConverter(Integer.class, Long.class, Integer2LongConverter.INSTANCE);

        //int2short
        addConverter(int.class, short.class, Integer2ShortConverter.INSTANCE);
        addConverter(int.class, Short.class, Integer2ShortConverter.INSTANCE);
        addConverter(Integer.class, short.class, Integer2ShortConverter.INSTANCE);
        addConverter(Integer.class, Long.class, Integer2ShortConverter.INSTANCE);

        //int2byte
        addConverter(int.class, byte.class, Integer2ByteConverter.INSTANCE);
        addConverter(int.class, Byte.class, Integer2ByteConverter.INSTANCE);
        addConverter(Integer.class, byte.class, Integer2ByteConverter.INSTANCE);
        addConverter(Integer.class, Byte.class, Integer2ByteConverter.INSTANCE);

        //int2bool
        addConverter(int.class, boolean.class, Integer2BooleanConverter.INSTANCE);
        addConverter(int.class, Boolean.class, Integer2BooleanConverter.INSTANCE);
        addConverter(Integer.class, boolean.class, Integer2BooleanConverter.INSTANCE);
        addConverter(Integer.class, Boolean.class, Integer2BooleanConverter.INSTANCE);

        //int2char
        addConverter(int.class, char.class, Integer2CharConverter.INSTANCE);
        addConverter(int.class, Character.class, Integer2CharConverter.INSTANCE);
        addConverter(Integer.class, char.class, Integer2CharConverter.INSTANCE);
        addConverter(Integer.class, Character.class, Integer2CharConverter.INSTANCE);

        //int2float
        addConverter(int.class, float.class, Integer2FloatConverter.INSTANCE);
        addConverter(int.class, Float.class, Integer2FloatConverter.INSTANCE);
        addConverter(Integer.class, float.class, Integer2FloatConverter.INSTANCE);
        addConverter(Integer.class, Float.class, Integer2FloatConverter.INSTANCE);

        //int2double
        addConverter(int.class, double.class, Integer2DoubleConverter.INSTANCE);
        addConverter(int.class, Double.class, Integer2DoubleConverter.INSTANCE);
        addConverter(Integer.class, double.class, Integer2DoubleConverter.INSTANCE);
        addConverter(Integer.class, Double.class, Integer2DoubleConverter.INSTANCE);

        //long

        //long2int
        addConverter(long.class, int.class, Long2IntegerConverter.INSTANCE);
        addConverter(long.class, Integer.class, Long2IntegerConverter.INSTANCE);
        addConverter(Long.class, int.class, Long2IntegerConverter.INSTANCE);
        addConverter(Long.class, Integer.class, Long2IntegerConverter.INSTANCE);

        //long2short
        addConverter(long.class, short.class, Long2ShortConverter.INSTANCE);
        addConverter(long.class, Short.class, Long2ShortConverter.INSTANCE);
        addConverter(Long.class, short.class, Long2ShortConverter.INSTANCE);
        addConverter(Long.class, Short.class, Long2ShortConverter.INSTANCE);

        //long2byte
        addConverter(long.class, byte.class, Long2ByteConverter.INSTANCE);
        addConverter(long.class, Byte.class, Long2ByteConverter.INSTANCE);
        addConverter(Long.class, byte.class, Long2ByteConverter.INSTANCE);
        addConverter(Long.class, Byte.class, Long2ByteConverter.INSTANCE);

        //long2bool
        addConverter(long.class, boolean.class, Long2BooleanConverter.INSTANCE);
        addConverter(long.class, Boolean.class, Long2BooleanConverter.INSTANCE);
        addConverter(Long.class, boolean.class, Long2BooleanConverter.INSTANCE);
        addConverter(Long.class, Boolean.class, Long2BooleanConverter.INSTANCE);

        //long2char
        addConverter(long.class, char.class, Long2CharConverter.INSTANCE);
        addConverter(long.class, Character.class, Long2CharConverter.INSTANCE);
        addConverter(Long.class, char.class, Long2CharConverter.INSTANCE);
        addConverter(Long.class, Character.class, Long2CharConverter.INSTANCE);

        //long2float
        addConverter(long.class, float.class, Long2FloatConverter.INSTANCE);
        addConverter(long.class, Float.class, Long2FloatConverter.INSTANCE);
        addConverter(Long.class, float.class, Long2FloatConverter.INSTANCE);
        addConverter(Long.class, Float.class, Long2FloatConverter.INSTANCE);

        //long2double
        addConverter(long.class, double.class, Long2DoubleConverter.INSTANCE);
        addConverter(long.class, Double.class, Long2DoubleConverter.INSTANCE);
        addConverter(Long.class, double.class, Long2DoubleConverter.INSTANCE);
        addConverter(Long.class, Double.class, Long2DoubleConverter.INSTANCE);

        //short
        //short2int
        addConverter(short.class, int.class, Short2IntegerConverter.INSTANCE);
        addConverter(short.class, Integer.class, Short2IntegerConverter.INSTANCE);
        addConverter(Short.class, int.class, Short2IntegerConverter.INSTANCE);
        addConverter(Short.class, Integer.class, Short2IntegerConverter.INSTANCE);

        //short2long
        addConverter(short.class, long.class, Short2LongConverter.INSTANCE);
        addConverter(short.class, Long.class, Short2LongConverter.INSTANCE);
        addConverter(Short.class, long.class, Short2LongConverter.INSTANCE);
        addConverter(Short.class, Long.class, Short2LongConverter.INSTANCE);

        //short2byte
        addConverter(short.class, byte.class, Short2ByteConverter.INSTANCE);
        addConverter(short.class, Byte.class, Short2ByteConverter.INSTANCE);
        addConverter(Short.class, byte.class, Short2ByteConverter.INSTANCE);
        addConverter(Short.class, Byte.class, Short2ByteConverter.INSTANCE);

        //short2bool
        addConverter(short.class, boolean.class, Short2BooleanConverter.INSTANCE);
        addConverter(short.class, Boolean.class, Short2BooleanConverter.INSTANCE);
        addConverter(Short.class, boolean.class, Short2BooleanConverter.INSTANCE);
        addConverter(Short.class, Boolean.class, Short2BooleanConverter.INSTANCE);

        //short2char
        addConverter(short.class, char.class, Short2CharConverter.INSTANCE);
        addConverter(short.class, Character.class, Short2CharConverter.INSTANCE);
        addConverter(Short.class, char.class, Short2CharConverter.INSTANCE);
        addConverter(Short.class, Character.class, Short2CharConverter.INSTANCE);

        //short2float
        addConverter(short.class, float.class, Short2FloatConverter.INSTANCE);
        addConverter(short.class, Float.class, Short2FloatConverter.INSTANCE);
        addConverter(Short.class, float.class, Short2FloatConverter.INSTANCE);
        addConverter(Short.class, Float.class, Short2FloatConverter.INSTANCE);

        //short2double
        addConverter(short.class, double.class, Short2DoubleConverter.INSTANCE);
        addConverter(short.class, Double.class, Short2DoubleConverter.INSTANCE);
        addConverter(Short.class, double.class, Short2DoubleConverter.INSTANCE);
        addConverter(Short.class, Double.class, Short2DoubleConverter.INSTANCE);

        //byte
        //byte2int
        addConverter(byte.class, int.class, Byte2IntegerConverter.INSTANCE);
        addConverter(byte.class, Integer.class, Byte2IntegerConverter.INSTANCE);
        addConverter(Byte.class, int.class, Byte2IntegerConverter.INSTANCE);
        addConverter(Byte.class, Integer.class, Byte2IntegerConverter.INSTANCE);

        //byte2long
        addConverter(byte.class, long.class, Byte2LongConverter.INSTANCE);
        addConverter(byte.class, Long.class, Byte2LongConverter.INSTANCE);
        addConverter(Byte.class, long.class, Byte2LongConverter.INSTANCE);
        addConverter(Byte.class, Long.class, Byte2LongConverter.INSTANCE);

        //byte2short
        addConverter(byte.class, short.class, Byte2ShortConverter.INSTANCE);
        addConverter(byte.class, Short.class, Byte2ShortConverter.INSTANCE);
        addConverter(Byte.class, short.class, Byte2ShortConverter.INSTANCE);
        addConverter(Byte.class, Short.class, Byte2ShortConverter.INSTANCE);

        //byte2bool
        addConverter(byte.class, boolean.class, Byte2BooleanConverter.INSTANCE);
        addConverter(byte.class, Boolean.class, Byte2BooleanConverter.INSTANCE);
        addConverter(Byte.class, boolean.class, Byte2BooleanConverter.INSTANCE);
        addConverter(Byte.class, Boolean.class, Byte2BooleanConverter.INSTANCE);

        //byte2char
        addConverter(byte.class, char.class, Byte2CharConverter.INSTANCE);
        addConverter(byte.class, Character.class, Byte2CharConverter.INSTANCE);
        addConverter(Byte.class, char.class, Byte2CharConverter.INSTANCE);
        addConverter(Byte.class, Character.class, Byte2CharConverter.INSTANCE);

        //byte2float
        addConverter(byte.class, float.class, Byte2FloatConverter.INSTANCE);
        addConverter(byte.class, Float.class, Byte2FloatConverter.INSTANCE);
        addConverter(Byte.class, float.class, Byte2FloatConverter.INSTANCE);
        addConverter(Byte.class, Float.class, Byte2FloatConverter.INSTANCE);

        //byte2double
        addConverter(byte.class, double.class, Byte2DoubleConverter.INSTANCE);
        addConverter(byte.class, Double.class, Byte2DoubleConverter.INSTANCE);
        addConverter(Byte.class, double.class, Byte2DoubleConverter.INSTANCE);
        addConverter(Byte.class, Double.class, Byte2DoubleConverter.INSTANCE);

        //boolean
        //bool2int
        addConverter(boolean.class, int.class, Boolean2IntegerConverter.INSTANCE);
        addConverter(boolean.class, Integer.class, Boolean2IntegerConverter.INSTANCE);
        addConverter(Boolean.class, int.class, Boolean2IntegerConverter.INSTANCE);
        addConverter(Boolean.class, Integer.class, Boolean2IntegerConverter.INSTANCE);

        //bool2long
        addConverter(boolean.class, long.class, Boolean2LongConverter.INSTANCE);
        addConverter(boolean.class, Long.class, Boolean2LongConverter.INSTANCE);
        addConverter(Boolean.class, long.class, Boolean2LongConverter.INSTANCE);
        addConverter(Boolean.class, Long.class, Boolean2LongConverter.INSTANCE);

        //bool2short
        addConverter(boolean.class, short.class, Boolean2ShortConverter.INSTANCE);
        addConverter(boolean.class, Short.class, Boolean2ShortConverter.INSTANCE);
        addConverter(Boolean.class, short.class, Boolean2ShortConverter.INSTANCE);
        addConverter(Boolean.class, Short.class, Boolean2ShortConverter.INSTANCE);

        //bool2char
        addConverter(boolean.class, char.class, Boolean2CharConverter.INSTANCE);
        addConverter(boolean.class, Character.class, Boolean2CharConverter.INSTANCE);
        addConverter(Boolean.class, char.class, Boolean2CharConverter.INSTANCE);
        addConverter(Boolean.class, Character.class, Boolean2CharConverter.INSTANCE);

        //bool2float
        addConverter(boolean.class, float.class, Boolean2FloatConverter.INSTANCE);
        addConverter(boolean.class, Float.class, Boolean2FloatConverter.INSTANCE);
        addConverter(Boolean.class, float.class, Boolean2FloatConverter.INSTANCE);
        addConverter(Boolean.class, Float.class, Boolean2FloatConverter.INSTANCE);

        //bool2double
        addConverter(boolean.class, double.class, Boolean2DoubleConverter.INSTANCE);
        addConverter(boolean.class, Double.class, Boolean2DoubleConverter.INSTANCE);
        addConverter(Boolean.class, double.class, Boolean2DoubleConverter.INSTANCE);
        addConverter(Boolean.class, Double.class, Boolean2DoubleConverter.INSTANCE);

        //char
        //char2int
        addConverter(char.class, int.class, Char2IntegerConverter.INSTANCE);
        addConverter(char.class, Integer.class, Char2IntegerConverter.INSTANCE);
        addConverter(Character.class, int.class, Char2IntegerConverter.INSTANCE);
        addConverter(Character.class, Integer.class, Char2IntegerConverter.INSTANCE);

        //char2long
        addConverter(char.class, long.class, Char2LongConverter.INSTANCE);
        addConverter(char.class, Long.class, Char2LongConverter.INSTANCE);
        addConverter(Character.class, long.class, Char2LongConverter.INSTANCE);
        addConverter(Character.class, Long.class, Char2LongConverter.INSTANCE);

        //char2short
        addConverter(char.class, short.class, Char2ShortConverter.INSTANCE);
        addConverter(char.class, Short.class, Char2ShortConverter.INSTANCE);
        addConverter(Character.class, short.class, Char2ShortConverter.INSTANCE);
        addConverter(Character.class, Short.class, Char2ShortConverter.INSTANCE);

        //char2bool
        addConverter(char.class, boolean.class, Char2BooleanConverter.INSTANCE);
        addConverter(char.class, Boolean.class, Char2BooleanConverter.INSTANCE);
        addConverter(Character.class, boolean.class, Char2BooleanConverter.INSTANCE);
        addConverter(Character.class, Boolean.class, Char2BooleanConverter.INSTANCE);

        //char2float
        addConverter(char.class, float.class, Char2FloatConverter.INSTANCE);
        addConverter(char.class, Float.class, Char2FloatConverter.INSTANCE);
        addConverter(Character.class, float.class, Char2FloatConverter.INSTANCE);
        addConverter(Character.class, Float.class, Char2FloatConverter.INSTANCE);

        //char2double
        addConverter(char.class, double.class, Char2DoubleConverter.INSTANCE);
        addConverter(char.class, Double.class, Char2DoubleConverter.INSTANCE);
        addConverter(Character.class, double.class, Char2DoubleConverter.INSTANCE);
        addConverter(Character.class, Double.class, Char2DoubleConverter.INSTANCE);

        //float
        //float2int
        addConverter(float.class, int.class, Float2IntegerConverter.INSTANCE);
        addConverter(float.class, Integer.class, Float2IntegerConverter.INSTANCE);
        addConverter(Float.class, int.class, Float2IntegerConverter.INSTANCE);
        addConverter(Float.class, Integer.class, Float2IntegerConverter.INSTANCE);

        //float2long
        addConverter(float.class, long.class, Float2LongConverter.INSTANCE);
        addConverter(float.class, Long.class, Float2LongConverter.INSTANCE);
        addConverter(Float.class, long.class, Float2LongConverter.INSTANCE);
        addConverter(Float.class, Long.class, Float2LongConverter.INSTANCE);

        //float2short
        addConverter(float.class, short.class, Float2ShortConverter.INSTANCE);
        addConverter(float.class, Short.class, Float2ShortConverter.INSTANCE);
        addConverter(Float.class, short.class, Float2ShortConverter.INSTANCE);
        addConverter(Float.class, Short.class, Float2ShortConverter.INSTANCE);

        //float2byte
        addConverter(float.class, byte.class, Float2ByteConverter.INSTANCE);
        addConverter(float.class, Byte.class, Float2ByteConverter.INSTANCE);
        addConverter(Float.class, byte.class, Float2ByteConverter.INSTANCE);
        addConverter(Float.class, Byte.class, Float2ByteConverter.INSTANCE);

        //float2bool
        addConverter(float.class, boolean.class, Float2BooleanConverter.INSTANCE);
        addConverter(float.class, Boolean.class, Float2BooleanConverter.INSTANCE);
        addConverter(Float.class, boolean.class, Float2BooleanConverter.INSTANCE);
        addConverter(Float.class, Boolean.class, Float2BooleanConverter.INSTANCE);

        //float2char
        addConverter(float.class, char.class, Float2CharConverter.INSTANCE);
        addConverter(float.class, Character.class, Float2CharConverter.INSTANCE);
        addConverter(Float.class, char.class, Float2CharConverter.INSTANCE);
        addConverter(Float.class, Character.class, Float2CharConverter.INSTANCE);

        //float2double
        addConverter(float.class, double.class, Float2DoubleConverter.INSTANCE);
        addConverter(float.class, Double.class, Float2DoubleConverter.INSTANCE);
        addConverter(Float.class, double.class, Float2DoubleConverter.INSTANCE);
        addConverter(Float.class, Double.class, Float2DoubleConverter.INSTANCE);

        //double
        //double2int
        addConverter(double.class, int.class, Double2IntegerConverter.INSTANCE);
        addConverter(double.class, Integer.class, Double2IntegerConverter.INSTANCE);
        addConverter(Double.class, int.class, Double2IntegerConverter.INSTANCE);
        addConverter(Double.class, Integer.class, Double2IntegerConverter.INSTANCE);

        //double2long
        addConverter(double.class, long.class, Double2LongConverter.INSTANCE);
        addConverter(double.class, Long.class, Double2LongConverter.INSTANCE);
        addConverter(Double.class, long.class, Double2LongConverter.INSTANCE);
        addConverter(Double.class, Long.class, Double2LongConverter.INSTANCE);

        //double2byte
        addConverter(double.class, byte.class, Double2ByteConverter.INSTANCE);
        addConverter(double.class, Byte.class, Double2ByteConverter.INSTANCE);
        addConverter(Double.class, byte.class, Double2ByteConverter.INSTANCE);
        addConverter(Double.class, Byte.class, Double2ByteConverter.INSTANCE);

        //double2short
        addConverter(double.class, short.class, Double2ShortConverter.INSTANCE);
        addConverter(double.class, Short.class, Double2ShortConverter.INSTANCE);
        addConverter(Double.class, short.class, Double2ShortConverter.INSTANCE);
        addConverter(Double.class, Short.class, Double2ShortConverter.INSTANCE);

        //double2bool
        addConverter(double.class, boolean.class, Double2BooleanConverter.INSTANCE);
        addConverter(double.class, Boolean.class, Double2BooleanConverter.INSTANCE);
        addConverter(Double.class, boolean.class, Double2BooleanConverter.INSTANCE);
        addConverter(Double.class, Boolean.class, Double2BooleanConverter.INSTANCE);

        //double2char
        addConverter(double.class, char.class, Double2CharConverter.INSTANCE);
        addConverter(double.class, Character.class, Double2CharConverter.INSTANCE);
        addConverter(Double.class, char.class, Double2CharConverter.INSTANCE);
        addConverter(Double.class, Character.class, Double2CharConverter.INSTANCE);

        //double2float
        addConverter(double.class, float.class, Double2FloatConverter.INSTANCE);
        addConverter(double.class, Float.class, Double2FloatConverter.INSTANCE);
        addConverter(Double.class, float.class, Double2FloatConverter.INSTANCE);
        addConverter(Double.class, Float.class, Double2FloatConverter.INSTANCE);

        //---------------------------- 数字与数字之间 start ------------------------------//

        //----------------------------  字符串与数字之间 start ------------------------------//
        //int类型
        com.google.common.base.Converter<String, Integer> string2intConverter = Ints.stringConverter();
        addGuavaConverter(String.class, Integer.class, string2intConverter);
        addGuavaConverter(String.class, int.class, string2intConverter);
        addGuavaConverter(Integer.class, String.class, string2intConverter.reverse());
        addGuavaConverter(int.class, String.class, string2intConverter.reverse());

        //long类型
        com.google.common.base.Converter<String, Long> string2longConverter = Longs.stringConverter();
        addGuavaConverter(String.class, Long.class, string2longConverter);
        addGuavaConverter(String.class, long.class, string2longConverter);
        addGuavaConverter(Long.class, String.class, string2longConverter.reverse());
        addGuavaConverter(long.class, String.class, string2longConverter.reverse());

        //short
        com.google.common.base.Converter<String, Short> string2shortConverter = Shorts.stringConverter();
        addGuavaConverter(String.class, Short.class, string2shortConverter);
        addGuavaConverter(String.class, short.class, string2shortConverter);
        addGuavaConverter(Short.class, String.class, string2shortConverter.reverse());
        addGuavaConverter(short.class, String.class, string2shortConverter.reverse());

        //byte
        addConverter(String.class, Byte.class, t -> Byte.decode((String) t));
        addConverter(String.class, byte.class, t -> Byte.decode((String) t));
        addConverter(Byte.class, String.class, Object::toString);
        addConverter(byte.class, String.class, Object::toString);

        //boolean
        addConverter(String.class, Boolean.class, t -> Boolean.valueOf((String) t));
        addConverter(String.class, boolean.class, t -> Boolean.valueOf((String) t));
        addConverter(Boolean.class, String.class, Object::toString);
        addConverter(boolean.class, String.class, Object::toString);

        //char
        addConverter(String.class, Character.class, String2CharConverter.INSTANCE);
        addConverter(String.class, char.class, String2CharConverter.INSTANCE);
        addConverter(Character.class, String.class, t -> Char2StringConverter.INSTANCE);
        addConverter(char.class, String.class, Char2StringConverter.INSTANCE);

        //float
        com.google.common.base.Converter<String, Float> string2floatConverter = Floats.stringConverter();
        addGuavaConverter(String.class, Float.class, string2floatConverter);
        addGuavaConverter(String.class, float.class, string2floatConverter);
        addGuavaConverter(Float.class, String.class, string2floatConverter.reverse());
        addGuavaConverter(float.class, String.class, string2floatConverter.reverse());

        //double
        com.google.common.base.Converter<String, Double> string2doubleConverter = Doubles.stringConverter();
        addGuavaConverter(String.class, Double.class, string2doubleConverter);
        addGuavaConverter(String.class, double.class, string2doubleConverter);
        addGuavaConverter(Double.class, String.class, string2doubleConverter.reverse());
        addGuavaConverter(double.class, String.class, string2doubleConverter.reverse());

        //bigdecimal
        addConverter(String.class, BigDecimal.class, String2BigDecimalConverter.INSTANCE);
        addConverter(BigDecimal.class, String.class, BigDecimal2StringConverter.INSTANCE);

        //biginteger
        addConverter(String.class, BigInteger.class, String2BigIntegerConverter.INSTANCE);
        addConverter(BigInteger.class, String.class, BigInteger2StringConverter.INSTANCE);

        //----------------------------  字符串与数字之间 end ------------------------------//

        //---------------------------- 时间相关 start ------------------------------//

        addConverter(String.class, Date.class, String2DateConverter.INSTANCE);
        addConverter(Date.class, String.class, Date2StringConverter.INSTANCE);

        //---------------------------- 时间相关 end ------------------------------//

    }

    @SuppressWarnings("unchecked")
    private static <S, D> void addGuavaConverter(Class<S> sourceClass, Class<D> destClass, com.google.common.base.Converter converter) {
        addConverter(sourceClass, destClass, (Converter) converter::convert);
    }

    /** 将指定源数据转换为目标类型数据 */
    @SuppressWarnings("unchecked")
    public static <S, D> D convert(S s, Class<D> destType) {
        return convert(s, destType, () -> null);
    }

    /** 将指定源数据转换为目标类型数据，如果源值为null, 则启用指定的数据提供器 */
    @SuppressWarnings("unchecked")
    public static <D, S> D convert(S s, Class<D> destType, Supplier<D> nullSourceSupplier) {
        if(s == null) {
            return nullSourceSupplier.get();
        }

        return convert(s, (Class<S>) s.getClass(), destType);
    }

    /** 将指定源数据转换为目标类型数据 */
    @SuppressWarnings("unchecked")
    public static <TS, S extends TS, D> D convert(S s, Class<TS> sourceType, Class<D> destType) {
        if(s == null) {
            return null;
        }

        //类型兼容,直接返回原值
        if(destType.isAssignableFrom(sourceType)) {
            return (D) s;
        }

        val converter = CONVERT_MAP.getOrDefault(sourceType, ImmutableMap.of()).get(destType);
        if(converter == null) {
            throw new RuntimeException("没有类型的转换器.源:" + sourceType + ",目的:" + destType);
        }

        return (D) converter.apply(s);
    }

    public static <S, D> void addConverter(Class<S> sourceClass, Class<D> destClass, Converter converter) {
        CONVERT_MAP.computeIfAbsent(sourceClass, t -> Maps.newIdentityHashMap())
                .put(destClass, converter);
    }
}
