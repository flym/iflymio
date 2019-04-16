package io.iflym.core.converter.generator;

import com.google.common.collect.Lists;
import io.iflym.core.converter.generator.converterhelper.*;
import io.iflym.core.converter.generator.internal.SpringOpTest;
import io.iflym.core.util.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Slf4j
public class ConverterHelperTest {

    @DataProvider
    public Object[][] p4testGenerateConverter() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 4;
            Object[] objects;

            //第1组 正常的按name匹配
            val from1 = From1.build("name1", "title1");
            val expectTo1 = To1.build("name1", "title1");//字段值均相同
            objects = new Object[paramLength];
            objects[0] = From1.class;
            objects[1] = To1.class;
            objects[2] = from1;
            objects[3] = expectTo1;
            objectsList.add(objects);

            //第2组 目的端有1个未能按name匹配，但字段上有From注解
            val from2 = From2.build("name2", "title2", "desc2");
            val expectTo2 = To2.build("name2", "title2", "desc2");//字段值均相同
            objects = new Object[paramLength];
            objects[0] = From2.class;
            objects[1] = To2.class;
            objects[2] = from2;
            objects[3] = expectTo2;
            objectsList.add(objects);

            //2组_V2 目的端有未能匹配的字段，此值转换后依旧为null
            val expectTo2_V2 = To2_V2.build("name2", "title2", "desc2", null);
            objects = new Object[paramLength];
            objects[0] = From2.class;
            objects[1] = To2_V2.class;
            objects[2] = from2;
            objects[3] = expectTo2_V2;
            objectsList.add(objects);

            //第3组 源端有1个未能按name匹配，按字段上有To注解
            val from3 = From3.build("name3", "title3", "extraDesc3");
            val expectTo3 = To3.build("name3", "title3", "extraDesc3");
            objects = new Object[paramLength];
            objects[0] = From3.class;
            objects[1] = To3.class;
            objects[2] = from3;
            objects[3] = expectTo3;
            objectsList.add(objects);

            //第4组 目的端类上有Mapping注解
            val from4 = From4.build("name4", "title4", "extraDesc4");
            val expectTo4 = To4.build("name4", "title4", "extraDesc4");
            objects = new Object[paramLength];
            objects[0] = From4.class;
            objects[1] = To4.class;
            objects[2] = from4;
            objects[3] = expectTo4;
            objectsList.add(objects);

            //第5组 源端类上有Mapping注解
            val from5 = From5.build("name5", "title5", "extraDesc5");
            val expectTo5 = To5.build("name5", "title5", "extraDesc5");
            objects = new Object[paramLength];
            objects[0] = From5.class;
            objects[1] = To5.class;
            objects[2] = from5;
            objects[3] = expectTo5;
            objectsList.add(objects);

            //第6组 目的端类上和字段上均有相同目的字段的注解，以字段上为准
            val from6 = From6.build("name6", "title6", "desc1_6", "desc2_6");
            val expectTo6 = To6.build("name6", "title6", "desc1_6");//最终值以源 desc1 为准
            objects = new Object[paramLength];
            objects[0] = From6.class;
            objects[1] = To6.class;
            objects[2] = from6;
            objects[3] = expectTo6;
            objectsList.add(objects);

            //第7组 目的端字段上有1个额外的From注解，源端上有1个额外的To注解
            val from7 = From7.build("name7", "title7", "desc1_7", "desc2_7");
            val expectTo7 = To7.build("name7", "title7", "desc1_7", "desc2_7");
            objects = new Object[paramLength];
            objects[0] = From7.class;
            objects[1] = To7.class;
            objects[2] = from7;
            objects[3] = expectTo7;
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Test(dataProvider = "p4testGenerateConverter")
    public void testGenerateConverter(Class<?> from, Class<?> to, Object fromObj, Object expectToObj) {
        val converter = ConverterHelper.generateConverter(from, to);
        ConvertUtils.addConverter(from, to, converter);

        val convertResult = ConvertUtils.convert(fromObj, to);

        Assert.assertEquals(convertResult, expectToObj);
    }

    /** 不启用spring相应支持类时仍正常工作 */
    @Test(dataProvider = "p4testGenerateConverter")
    public void testGenerateConverterWhenNoSpring(Class<?> from, Class<?> to, Object fromObj, Object expectToObj) {
        try{
            SpringOpTest.springClassNotExist();
            testGenerateConverter(from, to, fromObj, expectToObj);
        } finally {
            SpringOpTest.revertSpringClassExistValue();
        }
    }

    @Test
    public void testGenerateConverterUseMix() {
        //使用mixin类，分别对 FromMix1->ToMix1 FromMix2->ToMix2 进行映射，能够进行相应的映射处理，并且支持相应配置
        //第1组中, 使用MapField进行手动映射，第二组除显示地MapField外，在原类上的相应注解也会起作用

        val fromMix1 = FromMix1.build("name1", "title1", "desc1");
        val expectToMix1 = ToMix1.build("name1", "title1", "desc1");

        val fromMix2 = FromMix2.build("name2", "title2", "desc2_1", "desc2_2", "desc2_3");
        val expectToMix2 = ToMix2.build("name2", "title2", "desc2_1", "desc2_2", "desc2_3");

        val tuple3List = ConverterHelper.generateConverterUseMix(Mixin.class);
        tuple3List.forEach(t -> ConvertUtils.addConverter(t.t1, t.t2, t.t3));

        val resultToMix1 = ConvertUtils.convert(fromMix1, ToMix1.class);
        Assert.assertEquals(resultToMix1, expectToMix1);

        val resultToMix2 = ConvertUtils.convert(fromMix2, ToMix2.class);
        Assert.assertEquals(resultToMix2, expectToMix2);
    }

    public void testGenerateConverterUseMixWhenNoSpring() {
        try{
            SpringOpTest.springClassNotExist();
            testGenerateConverterUseMix();
        } finally {
            SpringOpTest.revertSpringClassExistValue();
        }
    }
}