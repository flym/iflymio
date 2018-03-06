package io.iflym.core.util;

import com.google.common.collect.Lists;
import io.iflym.core.util.ObjectUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/** Created by flym on 2017/11/1. */
public class ObjectUtilsTest {
    @DataProvider
    public Object[][] p4testIsEmpty() {
        List<Object[]> objectsList = Lists.newArrayList();

        //1 空对象
        Object[] objects = new Object[1];
        objects[0] = null;
        objectsList.add(objects);

        //2 空字符串 双引号
        objects = new Object[1];
        objects[0] = "";
        objectsList.add(objects);

        //3 空字符串 空格串
        objects = new Object[1];
        objects[0] = "  ";
        objectsList.add(objects);

        //4 空数组,基本对象
        objects = new Object[1];
        objects[0] = new int[0];
        objectsList.add(objects);

        //5 空数组 对象
        objects = new Object[1];
        objects[0] = new String[0];
        objectsList.add(objects);

        //6 空集合
        objects = new Object[1];
        objects[0] = Lists.newArrayList();
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    @Test(dataProvider = "p4testIsEmpty")
    public void testIsEmpty(Object input) throws Exception {
        Assert.assertTrue(ObjectUtils.isEmpty(input));
    }

    @DataProvider
    public Object[][] p4testIsNotEmpty() {
        List<Object[]> objectsList = Lists.newArrayList();

        //1 普通对象,不为null
        Object[] objects = new Object[1];
        objects[0] = new Object();
        objectsList.add(objects);

        //2 不是空的字符串
        objects = new Object[1];
        objects[0] = "   _";
        objectsList.add(objects);

        //3 不是空的数组
        objects = new Object[1];
        objects[0] = new Object[]{null, new Object()};
        objectsList.add(objects);

        //4 不是空的集合
        objects = new Object[1];
        objects[0] = Lists.newArrayList(new Object());
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试ObjectUtils.isEmpty 应该返回false的情况 */
    @Test(dataProvider = "p4testIsNotEmpty")
    public void testIsNotEmpty(Object input) throws Exception {
        Assert.assertFalse(ObjectUtils.isEmpty(input));
    }

}