package io.iflym.mybatis.test;

import com.google.common.collect.Lists;
import io.iflym.BaseTest;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.Page;
import io.iflym.mybatis.mapperx.AssertExt;
import io.iflym.mybatis.mapperx.domain.Element;
import io.iflym.mybatis.mapperx.mapper.ElementMapper;
import io.iflym.mybatis.util.DbUtils;
import lombok.experimental.var;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by luyi on 2017/12/6.
 */
public class DeleteTagTest extends BaseTest {

    @Resource
    private ElementMapper elementMapper;

    @BeforeClass
    private void prepareTable() throws Exception {
        DbUtils.createTable(jdbcTemplate, null, Element.TABLE_NAME, Element.TABLE_DDL);
    }

    /**
     * 测试获取单个对象
     */
    @Test
    public void testGetByKey() {
        Element element1 = new Element(1, "name1", 1);
        Element element2 = new Element(2, "name2", 0);
        save(elementMapper, element1, element2);

        //is_active=1的数据查询正常
        var result = elementMapper.get(Key.of(1));
        Assert.assertNotNull(result);

        //is_active=0的数据查询不存在
        result = elementMapper.get(Key.of(2));
        Assert.assertNull(result);
    }

    /**
     * 测试获取多个对象
     */
    @Test
    public void testGetMulti() {
        Element element3 = new Element(3, "name3", 1);
        Element element4 = new Element(4, "name4", 1);
        Element element5 = new Element(5, "name5", 0);
        save(elementMapper, element3, element4, element5);

        val expectValueList = Lists.newArrayList(element3.copy(), element4.copy());

        val keyList = Lists.newArrayList(Key.of(3), Key.of(4), Key.of(5));
        val resultList = elementMapper.getMulti(keyList);

        //查询数据只包含is_active=1的数据
        Assert.assertEquals(resultList.size(), 2);

        //数据相等
        AssertExt.assertContentEquals(resultList, expectValueList, Object::equals);
    }

    /**
     * 测试数据是否存在
     */
    @Test
    public void testExists() {
        Element element6 = new Element(6, "name6", 1);
        Element element7 = new Element(7, "name7", 0);
        save(elementMapper, element6, element7);

        //is_active=1的数据查询正常
        var result = elementMapper.exists(Key.of(6));
        Assert.assertTrue(result);

        //is_active=0的数据查询不存在
        result = elementMapper.exists(Key.of(7));
        Assert.assertFalse(result);
    }

    /**
     * 测试分页获取数据
     */
    @Test
    public void testListPage() {
        Element element8 = new Element(8, "name8", 1);
        Element element9 = new Element(9, "name9", 1);
        Element element10 = new Element(10, "name10", 1);
        Element element11 = new Element(11, "name11", 1);
        Element element12 = new Element(12, "name12", 1);
        Element element13 = new Element(13, "name13", 1);
        Element element14 = new Element(14, "name14", 1);
        Element element15 = new Element(15, "name15", 0);
        Element element16 = new Element(16, "name16", 0);

        List<Element> saveList = Lists.newArrayList(element8, element9, element10, element11, element12, element13, element14, element15, element16);

        save(elementMapper, saveList);

        var itemList = elementMapper.listPage(Page.NO_PAGE);
        //查询数据不包含is_active=0的数据
        Assert.assertEquals(itemList.size(), 7);
        saveList.remove(element15);
        saveList.remove(element16);
        AssertExt.assertContentContains(itemList, saveList, Object::equals);
    }

    /**
     * 测试条件查询
     */
    @Test
    public void testListCriteria() {
        Element element17 = new Element(17, "name17", 1);
        Element element18 = new Element(18, "name18", 0);

        save(elementMapper, element17, element18);

        //查询is_active正常数据
        var criteria = Criteria.of(Element.class);
        criteria.where(Criterion.eq("id", 17));
        var resultList = elementMapper.listCriteria(criteria);
        var expectList = Lists.newArrayList(element17.copy());
        AssertExt.assertContentEquals(resultList, expectList, Object::equals);

        //查询is_active数据不存在
        criteria = Criteria.of(Element.class);
        criteria.where(Criterion.eq("id", 18));
        resultList = elementMapper.listCriteria(criteria);
        Assert.assertEquals(resultList.size(), 0);
    }

    /**
     * 测试条件查询统计
     */
    @Test
    public void testCountCriteria() {
        Element element19 = new Element(19, "name19", 1);
        Element element20 = new Element(20, "name20", 1);
        Element element21 = new Element(21, "name21", 0);
        save(elementMapper, element19, element20, element21);

        var criteria = Criteria.of(Element.class);
        criteria.where(Criterion.between("id", 19, 20));
        var count = elementMapper.countCriteria(criteria);
        Assert.assertEquals(count, 2);

        criteria = Criteria.of(Element.class);
        criteria.where(Criterion.between("id", 19, 21));
        count = elementMapper.countCriteria(criteria);
        Assert.assertEquals(count, 2);//查询结果数仍然为2
    }

    /**
     * 测试删除功能
     */
    @Test
    public void testDelete() {
        Element element22 = new Element(22, "name22", 1);
        save(elementMapper, element22);

        //在删除之前能查询到此数据
        boolean result = elementMapper.exists(Key.of(22));
        Assert.assertTrue(result);

        elementMapper.delete(element22);

        //删除之后不能查询到此数据
        Element element = elementMapper.get(Key.of(22));
        Assert.assertNull(element);
    }


}
