package io.iflym.mybatis.test;

import com.google.common.collect.Lists;
import io.iflym.BaseTest;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.criteria.LikeTypeValue;
import io.iflym.mybatis.criteria.Order;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.Page;
import io.iflym.mybatis.domain.util.UpdateUtils;
import io.iflym.mybatis.example.Example;
import io.iflym.mybatis.exception.MybatisException;
import io.iflym.mybatis.mapperx.AssertExt;
import io.iflym.mybatis.mapperx.domain.Item;
import io.iflym.mybatis.mapperx.domain.SubItem;
import io.iflym.mybatis.mapperx.mapper.ItemMapper;
import io.iflym.mybatis.mapperx.mapper.LegacyMapper;
import io.iflym.mybatis.mapperx.mapper.SubItemMapper;
import io.iflym.mybatis.mapperx.mapper.mapperb.SubXItemMapper;
import io.iflym.mybatis.util.DbUtils;
import lombok.experimental.var;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对在mapper中提供的默认方法进行测试,保证其正常工作
 * Created by flym on 2017/11/8.
 */
public class MapperTest extends BaseTest {
    @Resource
    private ItemMapper itemMapper;
    @Resource
    private LegacyMapper legacyMapper;
    @Resource
    private SubItemMapper subItemMapper;

    @Autowired
    private SubXItemMapper subXItemMapperA;

    @Autowired
    private io.iflym.mybatis.mapperx.mapper.mappera.SubXItemMapper subXItemMapperB;

    @BeforeClass
    private void prepareTable() throws Exception {
        DbUtils.createTable(jdbcTemplate, null, Item.TABLE_NAME, Item.TABLE_DDL);
    }

    /**
     * 测试保存时带主键信息
     */
    @Test
    public void testSaveWithId() throws Exception {
        Item item = new Item();
        item.setId(-1);
        item.setUsername("abc");

        //保存之前没有数据
        String where = "id = -1 and username = 'abc'";
        long count = countRowsInTableWhere(Item.TABLE_NAME, where);
        Assert.assertEquals(count, 0);

        itemMapper.save(item);

        //保存之后有数据
        count = countRowsInTableWhere(Item.TABLE_NAME, where);
        Assert.assertEquals(count, 1);
    }

    /**
     * 测试保存时不带主键信息,由数据库自动生成
     */
    @Test
    public void testSaveWithoutId() {
        Item item = new Item();
        item.setUsername("abc2");

        //保存之前没有数据
        String where = "username = 'abc2'";
        long count = countRowsInTableWhere(Item.TABLE_NAME, where);
        Assert.assertEquals(count, 0);

        itemMapper.save(item);

        //保存之后, 相应的item有主键信息
        Assert.assertTrue(item.key().fullfill());

        //保存之后有数据
        count = countRowsInTableWhere(Item.TABLE_NAME, where);
        Assert.assertEquals(count, 1);
    }

    /**
     * 测试根据主键对象相应的数据
     */
    @Test
    public void testGetByKey() {
        Item item = new Item(-2, "abc3", 20, 1);
        itemMapper.save(item);

        Key key = Key.of(-2);
        val result = itemMapper.get(key);
        Assert.assertNotNull(result);

        //验证相应的数据与原来保存的一致
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getUsername(), item.getUsername());

    }

    /**
     * 测试获取多个对象的情况
     */
    @Test
    public void testGetMulti() {
        Item item4 = new Item(-4, "abc4", 4, 1);
        Item item5 = new Item(-5, "abc5", 5, 1);
        save(itemMapper, item4, item5);

        val expectValueList = Lists.newArrayList(item4.copy(), item5.copy());

        val keyList = Lists.newArrayList(Key.of(-4), Key.of(-5));
        val resultList = itemMapper.getMulti(keyList);

        //数目相同
        Assert.assertEquals(resultList.size(), 2);

        //数据相等
        AssertExt.assertContentEquals(resultList, expectValueList, Object::equals);
    }

    /**
     * 测试判断数据存在的情况
     */
    @Test
    public void testExists() {
        Item item6 = new Item(-6, "abc6", 6, 1);
        save(itemMapper, item6);

        //1个不存在的key值,那么肯定不存在
        boolean value = itemMapper.exists(Key.of(-1));
        Assert.assertFalse(value);

        //之前保存过的数据, 查找数据应该存在
        value = itemMapper.exists(Key.of(-6));
        Assert.assertTrue(value);
    }

    /**
     * 测试分页查询数据的情况
     */
    @Test
    public void testListPage() {
        val item7 = new Item(-7, "abc7", 7, 1);
        val item8 = new Item(-8, "abc8", 8, 1);
        val item9 = new Item(-9, "abc9", 9, 1);
        val item10 = new Item(-10, "abc10", 10, 1);
        val item11 = new Item(-11, "abc11", 11, 1);
        val item12 = new Item(-12, "abc12", 12, 1);
        val item13 = new Item(-13, "abc13", 13, 1);
        val item14 = new Item(-14, "abc14", 14, 1);
        List<Item> saveList = Lists.newArrayList(item7, item8, item9, item10, item11, item12, item13, item14);

        save(itemMapper, saveList);

        //不分页的情况
        var itemList = itemMapper.listPage(Page.NO_PAGE);
        AssertExt.assertContentContains(itemList, saveList, Object::equals);

        //分页的情况
        //第2页,每页2条, 应该能查询出数据
        Page page = new Page(2, 2);
        itemList = itemMapper.listPage(page);
        //这里能查询出数据,但不能保证查询出来的数据与预期一致,因为这里仅要求查询出来的数据是2条即可
        Assert.assertEquals(itemList.size(), 2);
    }

    /**
     * 测试带条件查询的情况
     */
    @Test
    public void testListCriteria() {
        val item15 = new Item(-15, "abc15", 15, 1);
        val item16 = new Item(-16, "abc16", 16, 1);
        val item17 = new Item(-17, "abc17", 17, 1);
        val item18 = new Item(-18, "abc18", 18, 1);
        val item19 = new Item(-19, "abc19", 19, 1);

        save(itemMapper, item15, item16, item17, item18, item19);

        //主键相等性查询
        var criteria = Criteria.of(Item.class);
        criteria.where(Criterion.eq("id", -17));
        var resultList = itemMapper.listCriteria(criteria);
        var expectList = Lists.newArrayList(item17.copy());
        AssertExt.assertContentEquals(resultList, expectList, Object::equals);

        //多条件查询
        criteria = Criteria.of(Item.class);
        criteria.where(Criterion.like("username", "abc", LikeTypeValue.START))
                .where(Criterion.eq("age", 18));
        resultList = itemMapper.listCriteria(criteria);
        expectList = Lists.newArrayList(item18.copy());
        AssertExt.assertContentEquals(resultList, expectList, Object::equals);

        //or查询
        criteria = Criteria.of(Item.class);
        criteria.where(Criterion.or(Criterion.eq("id", -15), Criterion.eq("id", -16)));
        resultList = itemMapper.listCriteria(criteria);
        expectList = Lists.newArrayList(item15.copy(), item16.copy());
        AssertExt.assertContentEquals(resultList, expectList, Object::equals);

        //带排序,带分页
        //场景, 主键在-15至-18之间,即4条记录,按username倒序, 并且查询第二页数据, 那么期望值即为 -15, -16
        criteria = Criteria.of(Item.class);
        criteria.where(Criterion.between("id", -18, -15));
        criteria.order(Order.desc("username"));
        criteria.limit(new Page(2, 2));

    }

    /**
     * 测试带条件查询时的总数是否正确
     */
    @Test
    public void testCountCriteria() {
        val item20 = new Item(-20, "abc20", 20, 1);
        val item21 = new Item(-21, "abc21", 21, 1);
        val item22 = new Item(-22, "abc22", 22, 1);
        val item23 = new Item(-23, "abc23", 23, 1);
        val item24 = new Item(-24, "abc24", 24, 1);
        save(itemMapper, item20, item21, item22, item23, item24);

        //多条件查询 age 21至23 username 大于等于abc22, 那么结果就 1条 即 -12
        var criteria = Criteria.of(Item.class);
        criteria.where(Criterion.between("age", 21, 23))
                .where(Criterion.ge("username", "abc22"));
        int count = itemMapper.countCriteria(criteria);
        int expectValue = 2;
        Assert.assertEquals(count, expectValue);


        //条件中带limit查询, 查总数时会清除此条件 这里查询第一页,限制1条,但查总数时应该取消此限制条件
        criteria.limit(new Page(1, 1));
        count = itemMapper.countCriteria(criteria);
        //期望结果仍然是2条,
        expectValue = 2;
        Assert.assertEquals(count, expectValue);

    }

    /**
     * 验证删除的场景
     */
    @Test
    public void testDelete() {
        val item25 = new Item(-25, "abc25", 25, 1);
        save(itemMapper, item25);

        //在删除之前能查询到此数据
        int cnt = countRowsInTableWhere(Item.TABLE_NAME, "id = -25");
        Assert.assertEquals(cnt, 1);

        itemMapper.delete(item25);

        //删除之后不能查询到此数据
        Item item = itemMapper.get(Key.of(-25));
        Assert.assertNull(item);
    }

    /**
     * 验证进行数据更新的场景
     */
    @Test
    public void testUpdate() {
        val item26 = new Item(-26, "abc26", 26, 1);
        save(itemMapper, item26);

        //在修改之前根据username能查询到此数据
        int cnt = countRowsInTableWhere(Item.TABLE_NAME, "username = 'abc26'");
        Assert.assertEquals(cnt, 1);

        //修改其username为 _abc26
        item26.setAge(11);
        item26.upmark();
        item26.setUsername("_abc26");
        itemMapper.update(item26);
        val test = itemMapper.get(Key.of(-26));
        System.out.println("result: " + test.getAge());

        //修改之后,原来的查询不能查询数据
        cnt = countRowsInTableWhere(Item.TABLE_NAME, "username = 'abc26'");
        Assert.assertEquals(cnt, 0);

        //按修改之后的值能查询到结果
        cnt = countRowsInTableWhere(Item.TABLE_NAME, "username = '_abc26'");
        Assert.assertEquals(cnt, 1);

        //修改之后的数据值按主键查询能够查询数据,
        val value = itemMapper.get(Key.of(-26));
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getUsername(), "_abc26");

        //支持update为null
        item26.upmark();
        item26.setUsername(null);
        itemMapper.update(item26);
        val testUpdate = itemMapper.get(Key.of(-26));
        Assert.assertNull(testUpdate.getUsername(), "不能修改为null:" + testUpdate.getUsername());
    }

    /** 验证调用update时并没有调用upMark的场景 */
    @Test(expectedExceptions = MybatisException.class)
    public void testUpdateNotInvokeUpmarkShouldFail() {
        val item26 = new Item(-26, "abc26", 26, 1);
        save(itemMapper, item26);

        item26.setAge(-27);
        item26.setUsername("abc27");

        itemMapper.update(item26);
    }

    /**
     * 验证partTree查询的场景
     */
    @Test
    public void testPartTree() {
        val item27 = new Item(-27, "abc27", 27, 1);
        save(itemMapper, item27);

        //根据用户名应该能查询出
        val result = itemMapper.getByUsername("abc27");
        Assert.assertNotNull(result);
        Assert.assertEquals(result, item27);
    }

    /**
     * 验证partTree count查询的场景
     */
    @Test
    public void testCountPartTree() {
        val item27 = new Item(-27, "abc27", 27, 1);
        save(itemMapper, item27);

        //根据用户名应该能查询出
        val result = itemMapper.countByUsername("abc27");
        Assert.assertEquals(1, Math.toIntExact(result));
        val result2 = itemMapper.countById(-27);
        Assert.assertNotNull(result2);
        Assert.assertEquals(1, Math.toIntExact(result2));
    }

    /**
     * 验证partTree查询时查询列表的场景
     */
    @Test
    public void testPartTreeList() {
        val item28 = new Item(-28, "abc28", 28, 1);
        val item29 = new Item(-29, "abc29", 29, 1);
        val item30 = new Item(-30, "abc30", 30, 1);
        save(itemMapper, item28, item29, item30);

        //根据2个不同的条件,能够查询出2条数据,
        //使用-28和abc30 2个不同的条件, 并且按age倒序,最后的结果应该是 item30和item28
        val resultList = itemMapper.queryByIdOrUsernameEqOrderByAgeDesc(-28, "abc30");
        val expectList = Lists.newArrayList(item30.copy(), item28.copy());

        Assert.assertEquals(resultList, expectList);
    }

    /**
     * 验证partTree查询时有分页的场景
     */
    @Test
    public void testPartTreePaged() {
        val item31 = new Item(-31, "abc31", 31, 1);
        val item32 = new Item(-32, "abc32", 32, 1);
        val item33 = new Item(-33, "abc33", 33, 1);
        val item34 = new Item(-34, "abc34", 34, 1);
        val item35 = new Item(-35, "abc35", 35, 1);
        save(itemMapper, item31, item32, item33, item34, item35);

        //使用条件 age > 32 每页1条,第2页, 结果为 34, 总数为3条
        Page page = new Page(2, 1);
        val resultList = itemMapper.listByAgeGt(32, page);
        val expectList = Lists.newArrayList(item34.copy());

        Assert.assertEquals(resultList, expectList);

        //验证总数
        Assert.assertEquals(page.getCount(), 3);
    }

    @Test
    public void testGetWithType() {
        Item item = new Item(-2, "abc3", 2, 1);
        itemMapper.save(item);

        Key key = Key.of(-2);
        Entity result = itemMapper.get(key, SubItem.class);
        //返回结果不为空
        Assert.assertNotNull(result);
        val cacheResult = itemMapper.get(key, SubItem.class);//used debug test ms cache
        //返回结果不为空
        Assert.assertNotNull(cacheResult);
    }

    @Test
    public void testGetMultiWithType() {
        Item item4 = new Item(-4, "abc4", 4, 1);
        Item item5 = new Item(-5, "abc5", 5, 1);
        save(itemMapper, item4, item5);

        val expectValueList = Lists.newArrayList(item4.copy(), item5.copy());

        val keyList = Lists.newArrayList(Key.of(-4), Key.of(-5));
        val resultList = itemMapper.getMulti(keyList, SubItem.class);

        //数目相同
        Assert.assertEquals(resultList.size(), 2);

    }

    @Test
    public void testListPageWithPage() {
        val item7 = new Item(-7, "abc7", 7, 1);
        val item8 = new Item(-8, "abc8", 8, 1);
        val item9 = new Item(-9, "abc9", 9, 1);
        val item10 = new Item(-10, "abc10", 10, 1);
        val item11 = new Item(-11, "abc11", 11, 1);
        val item12 = new Item(-12, "abc12", 12, 1);
        val item13 = new Item(-13, "abc13", 13, 1);
        val item14 = new Item(-14, "abc14", 14, 1);
        List<Item> saveList = Lists.newArrayList(item7, item8, item9, item10, item11, item12, item13, item14);

        save(itemMapper, saveList);

        //不分页的情况
        var itemList = itemMapper.listPage(Page.NO_PAGE, SubItem.class);
        Assert.assertEquals(itemList.size(), saveList.size());
        //返回类型为指定类型
        Assert.assertTrue(itemList.get(0) != null);

        //分页的情况
        //第2页,每页2条, 应该能查询出数据
        Page page = new Page(2, 2);
        val itemList2 = itemMapper.listPage(page, SubItem.class);
        //这里能查询出数据,但不能保证查询出来的数据与预期一致,因为这里仅要求查询出来的数据是2条即可
        Assert.assertEquals(itemList2.size(), 2);
        //返回类型为指定类型
        Assert.assertTrue(itemList2.get(0) != null);
        System.out.println("result: " + itemList.get(0));
    }


    @Test
    public void testUpdateByExample() {
        val item15 = new Item(-15, "abc15", 15, 1);
        val item16 = new Item(-16, "abc16", 16, 1);
        val item17 = new Item(-17, "abc17", 17, 1);
        val item18 = new Item(-18, "abc18", 18, 1);
        val item19 = new Item(-19, "abc19", 19, 1);
        val item20 = new Item(-20, "abc20", 20, 0);
        save(itemMapper, item15, item16, item17, item18, item19, item20);

        //更新数据
        var userName = "abc15_example";
        item15.setUsername(userName);
        itemMapper.updateByExample(Example.ofAll(item15));

        //根据id查询数据后，校验username是否被成功更新
        var item = itemMapper.get(Key.of(-15));
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getUsername(), userName);

        //测试excludeProperties功能
        userName = "abc16_example";
        item16.setUsername(userName);
        item16.setAge(18);
        var example = Example.ofAll(item16).nonZero().nonNull().exclude("age");
        itemMapper.updateByExample(example);
        item = itemMapper.get(Key.of(-16));
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getUsername(), userName);
        Assert.assertEquals(item.getAge(), 16);

        //测试createNonZeroOrNull，不更新null或者0值
        userName = "abc17_example";
        item17.setUsername(userName);
        item17.setAge(0);
        example = Example.of(item17).nonNullAndZero();
        itemMapper.updateByExample(example);
        item = itemMapper.get(Key.of(-17));
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getUsername(), userName);
        Assert.assertEquals(item.getAge(), 17);

        //测试createNonZero，不更新0值
        userName = "abc18_example";
        item18.setUsername(userName);
        item18.setAge(0);
        example = Example.of(item18).nonZero();
        itemMapper.updateByExample(example);
        item = itemMapper.get(Key.of(-18));
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getUsername(), userName);
        Assert.assertEquals(item.getAge(), 18);

        //测试includedProperties
        userName = "abc19_example";
        item19.setUsername(userName);
        item19.setAge(0);
        example = Example.of(item19).must("isActive", "age");
        itemMapper.updateByExample(example);
        item = itemMapper.get(Key.of(-19));
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getUsername(), "abc19");
        Assert.assertEquals(item.getAge(), 0);

        //测试includedProperties && exclude && propertySeletor
        userName = "abc20_example";
        item20.setUsername(userName);
        item20.setAge(0);
        item20.setIsActive(1);
        example = Example.of(item20).must("isActive", "age", "userName").exclude("userName");
        itemMapper.updateByExample(example);
        item = itemMapper.get(Key.of(-20));
        Assert.assertNotNull(item);
        //因为must优先级最高,所以为更新后的值
        Assert.assertEquals(item.getUsername(), "abc20_example");
        //因为包含此值,所以值应为0
        Assert.assertEquals(item.getAge(), 0);
        Assert.assertEquals(item.getIsActive(), 1);

    }

    /** 验证兼容性mapper正常工作情况 */
    @Test
    public void testLegacyWork() {
        val item21 = new Item(-21, "abc21", 21, 0);
        save(itemMapper, item21);

        val result = legacyMapper.getById(-21);
        Assert.assertNotNull(result);

        Assert.assertEquals(result, item21);
    }

    /** 验证当一个子mapper继承标准mapper之后 仍然能够工作 */
    @Test
    public void testSubMapperWork() {
        val id = -22;
        val username = "abc22";
        val item22 = new Item(id, username, 22, 1);
        save(itemMapper, item22);

        //使用标准的信息能够查询
        var result = itemMapper.get(Key.of(id));
        Assert.assertEquals(result, item22);

        result = itemMapper.getByUsername(username);
        Assert.assertEquals(result, item22);

        //使用继承的子类仍然能查询
        result = subItemMapper.get(Key.of(id));
        Assert.assertEquals(result, item22);

        result = subItemMapper.queryWithUsername(username);
        Assert.assertEquals(result, item22);

        //使用默认的defaultResultMap 不加前缀
        result = subItemMapper.queryWithUsernameUseDefaultMap(username);
        Assert.assertEquals(result, item22);
    }

    /** 验证同一个mapper名字的类继承统一的父类都能正常工作 */
    @Test
    public void testSameSubMapperWork() {
        val id = -23;
        val username = "abc23";
        val item23 = new Item(id, username, 23, 1);
        save(itemMapper, item23);

        val qitem1 = subXItemMapperA.queryWithUsername(username);
        val qitem2 = subXItemMapperB.queryWithUsername(username);

        Assert.assertEquals(item23, qitem1);
        Assert.assertEquals(qitem1, qitem2);

        //验证其使用标准的getbyid也能查询
        val qitem3 = subXItemMapperA.get(Key.of(id));
        val qitem4 = subXItemMapperB.get(Key.of(id));

        Assert.assertEquals(item23, qitem3);
        Assert.assertEquals(qitem3, qitem4);
    }

    /** 验证相应的nonEmpty在修改时不会对传入的空参数作处理 */
    @Test
    public void testNonEmptyUpdate() {
        val id = -24;
        val username = "abc24";

        val item24 = new Item(id, username, 24, 1);
        save(itemMapper, item24);

        //正常修改，能够成功修改
        item24.upmark();
        item24.setUsername("abc24-2");
        itemMapper.update(item24);

        Item qitem = itemMapper.get(Key.of(id));
        Assert.assertEquals(qitem.getUsername(), "abc24-2");

        val item25 = new Item(-25, "abc25", 25, 1);
        save(itemMapper, item25);

        //空修改，无变化
        item25.upmark();
        //noinspection Convert2Lambda
        UpdateUtils.nonEmptyUpdate(new Runnable() {
            @Override
            public void run() {
                item25.setUsername(null);
                item25.setAge(-25);
            }
        });
        itemMapper.update(item25);

        qitem = itemMapper.get(Key.of(-25));
        //用户名无修改
        Assert.assertEquals(qitem.getUsername(), "abc25");
        //age 有变化
        Assert.assertEquals(qitem.getAge(), -25);
    }
}