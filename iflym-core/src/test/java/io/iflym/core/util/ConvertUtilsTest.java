package io.iflym.core.util;

import com.google.common.collect.Lists;
import io.iflym.core.util.converutils.Dest1;
import io.iflym.core.util.converutils.Source1;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 对ConverterUtils进行测试
 * Created by flym on 2017/11/8.
 */
@Slf4j
public class ConvertUtilsTest {
    @DataProvider
    public Object[][] p4testConvert() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 4;
            Object[] objects;

            //1 类型相兼容, 则直接返回原对象
            objects = new Object[paramLength];
            val source1 = new Source1("username1");
            objects[0] = source1;
            objects[1] = Source1.class;
            objects[2] = Dest1.class;
            objects[3] = source1;
            objectsList.add(objects);

            //2 兼容不兼容,则作相应的转换
            //这里因为工具类已内置数字之间的转换,这里进行数字到字符串的转换
            objects = new Object[paramLength];
            objects[0] = 123;
            objects[1] = Integer.class;
            objects[2] = String.class;
            objects[3] = "123";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 测试正常的转换过程 */
    @Test(dataProvider = "p4testConvert")
    @SuppressWarnings("unchecked")
    public void testConvert(Object source, Class sourceType, Class destType, Object expectValue) throws Exception {
        val result = ConvertUtils.convert(source, sourceType, destType);

        Assert.assertEquals(result, expectValue);
    }

    /** 测试转换会失败的场景 */
    @Test(expectedExceptions = Exception.class)
    @SuppressWarnings("unchecked")
    public void testConvertFail() throws Exception {
        //相应的类型之间并没有转换器, 则会直接转换失败
        Dest1 dest1 = new Dest1("usernam2");
        val destType = Source1.class;
        val sourceType = dest1.getClass();

        ConvertUtils.convert(dest1, (Class) sourceType, destType);
    }

}