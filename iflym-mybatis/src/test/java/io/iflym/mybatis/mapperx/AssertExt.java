package io.iflym.mybatis.mapperx;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Created by flym on 2017/11/8. */
public class AssertExt {

    /** 用于判断2个集合在内容上是否是相同的 */
    public static <A, B> void assertContentEquals(Collection<A> actual, Collection<B> expected, BiPredicate<A, B> equivalence) {
        if(actual == expected) {
            return;
        }

        if(actual == null || expected == null) {
            throw new AssertionError("Collections not equal: expected: " + expected + " and actual: " + actual);
        }
        assertEquals(actual.size(), expected.size(), " lists don't have the same size");

        //判断方法,使用两个迭代器相互删除,在迭代完之后,保证两边都是空的
        List<A> temp1List = Lists.newArrayList(actual);
        List<B> temp2List = Lists.newArrayList(expected);

        for(Iterator<A> itA = temp1List.iterator(); itA.hasNext(); ) {
            boolean removed = false;
            A a = itA.next();
            for(Iterator<B> itB = temp2List.iterator(); itB.hasNext(); ) {
                B b = itB.next();
                if(equivalence.test(a, b)) {
                    itB.remove();
                    removed = true;
                    break;
                }
            }

            assertTrue(removed, "Collection not equal: " + expected + " and " + actual);
            itA.remove();
        }

        assertTrue(temp1List.isEmpty(), "Collection not equal: " + expected + " and " + actual);
    }

    /** 用于判断实际的集合在内容上是否包括预期的集合信息 */
    public static <A, B> void assertContentContains(Collection<A> actual, Collection<B> expected, BiPredicate<A, B> equivalence) {
        if(actual == expected) {
            return;
        }

        //都为Null，两Null不相等
        if(actual == null || expected == null) {
            throw new AssertionError("Collections not equal: expected: " + expected + " and actual: " + actual);
        }

        Collection<B> removed = Lists.newArrayList(expected);
        //循环子集B
        //拿出B中某个元素，如果在A集合中，能找到之相等的元素，则删除B中的此元素
        expected.stream().filter(b -> actual.stream().anyMatch(input -> equivalence.test(input, b)))
                .forEach(removed::remove);

        if(removed.size() != 0) {
            throw new AssertionError("Collection actual not fully included expected, except:" + removed);
        }
    }
}
