package io.iflym.core.util;

import io.iflym.core.util.methodutils.SecondMap;
import io.iflym.core.util.methodutils.ThirdMap;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Created by flym on 2017/11/1. */
public class MethodUtilsTest {
    @Test
    public void testInvokeSpecial() {
        //thirdMap正常的继承父类调用, 那么相应的随机值肯定会发生变化
        ThirdMap thirdMap = new ThirdMap();
        thirdMap.put("abc", "abc");
        Assert.assertNotNull(thirdMap.randomStr);

        //secondMap使用invokeSpecial直接调用hashmap, 那么相应的随机值不会变化,仍然为null,即没有调用父类的方法
        SecondMap secondMap = new SecondMap();
        secondMap.put("abc", "abc");
        Assert.assertNull(secondMap.randomStr);
    }
}
