package io.iflym.core.util;

import com.google.common.collect.Lists;
import io.iflym.core.util.ListUtils;
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

}