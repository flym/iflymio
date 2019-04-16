package io.iflym.core.util;

import com.google.common.collect.Lists;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Created by flym on 2017/11/8. */
public class ListUtilsTest {
    @Test
    public void testFilter() throws Exception {
        //构建一个数字集合, 过滤掉 >50 并且 < 10 的数字,最终结果应与预期相一致
        List<Integer> sourceList = Lists.newArrayList(1, 2, 3, 20, 30, 70, 80, 90);
        //剩下的为小于= 50的
        Predicate<Integer> predicate1 = t -> t <= 50;
        //剩下的为>= 10的
        Predicate<Integer> predicate2 = t -> t >= 10;
        List<Integer> expectValue = Lists.newArrayList(20, 30);

        List<Integer> result = ListUtils.filter(sourceList, predicate1, predicate2);
        Assert.assertEquals(result, expectValue);
    }

    @Test
    public void testMap() throws Exception {
        //构建一个全是字符串的集合,转换为数字,并且追加特别的数字, 最终的结果应该与预期相一致
        List<String> sourceList = Lists.newArrayList("123", "124", "125");
        Function<String, Integer> map = t -> Integer.parseInt(t) + 1000;
        List<Integer> expectValue = Lists.newArrayList(1123, 1124, 1125);

        List<Integer> result = ListUtils.map(sourceList, map);

        Assert.assertEquals(result, expectValue);
    }

    @Test
    public void testFindFirst() throws Exception {
        //构建一个数字集合，查找第1个>=10和<=50的数字，最终结果应与预期一致
        List<Integer> sourceList = Lists.newArrayList(1, 2, 3, 20, 30, 70, 80, 90);
        //剩下的为小于= 50的
        Predicate<Integer> predicate1 = t -> t <= 50;
        //剩下的为>= 10的
        Predicate<Integer> predicate2 = t -> t >= 10;

        //期望值应该为20
        Integer expect = 20;

        val result = ListUtils.findFirst(sourceList, predicate1, predicate2);
        Assert.assertEquals(result, expect);

        //查找>100的数字，没有找到，期望值为null
        Predicate<Integer> predicate3 = t -> t > 100;
        val gt100Result = ListUtils.findFirst(sourceList, predicate3);
        Assert.assertNull(gt100Result);
    }

    @Test
    public void testFindFirstOptional() throws Exception {
        //构建一个数字集合，查找第1个>=10和<=50的数字，最终结果应与预期一致
        List<Integer> sourceList = Lists.newArrayList(1, 2, 3, 20, 30, 70, 80, 90);
        //剩下的为小于= 50的
        Predicate<Integer> predicate1 = t -> t <= 50;
        //剩下的为>= 10的
        Predicate<Integer> predicate2 = t -> t >= 10;

        //期望值应该为20
        Integer expect = 20;

        val resultOptional = ListUtils.findFirstOptional(sourceList, predicate1, predicate2);
        Assert.assertTrue(resultOptional.isPresent(), "没有找到期望的数据");
        Assert.assertEquals(resultOptional.get(), expect);

        //查找>100的数字，没有找到，期望值为empty
        Predicate<Integer> predicate3 = t -> t > 100;
        val gt100ResultOptional = ListUtils.findFirstOptional(sourceList, predicate3);
        if(gt100ResultOptional.isPresent()) {
            throw new AssertionError("期望不会找到数据，实际上找到了:" + gt100ResultOptional.get());
        }
    }

}