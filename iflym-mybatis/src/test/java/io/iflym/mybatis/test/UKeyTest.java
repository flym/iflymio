package io.iflym.mybatis.test;

import com.google.common.collect.Lists;
import io.iflym.core.util.CloneUtils;
import io.iflym.BaseTest;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.UniqueKey;
import io.iflym.mybatis.domain.info.EntityInfoHolder;
import io.iflym.mybatis.domain.util.ColumnInfoUtils;
import io.iflym.mybatis.example.Example;
import io.iflym.mybatis.exception.MybatisException;
import io.iflym.mybatis.mapperx.domain.UKeyElement;
import io.iflym.mybatis.mapperx.domain.UkeySubElement;
import io.iflym.mybatis.mapperx.mapper.UKeyElementMapper;
import io.iflym.mybatis.util.DbUtils;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luyi
 */
@Slf4j
public class UKeyTest extends BaseTest {

    @Resource
    private UKeyElementMapper uKeyElementMapper;

    @BeforeClass
    private void prepareTable() throws Exception {
        DbUtils.createTable(jdbcTemplate, null, UKeyElement.TABLE_NAME, UKeyElement.TABLE_DDL);
    }

    @Test
    public void testGetByUKey() {
        val ukElement1 = new UKeyElement(1, "attr1_1", 1, "attr3_1", 1L, "attr5_1", "attr6_1");
        val ukElement2 = new UKeyElement(2, "attr1_2", 2, "attr3_2", 2L, "attr5_2", "attr6_2");
        val ukElement3 = new UKeyElement(3, "attr1_3", 3, "attr3_3", 3L, "attr5_3", "attr6_3");
        val ukElement4 = new UKeyElement(4, "attr1_4", 4, "attr3_4", 4L, "attr5_4", "attr6_4");
        val ukElement5 = new UKeyElement(5, "attr1_5", 5, "attr3_5", 5L, "attr5_5", "attr6_5");
        val ukElement6 = new UKeyElement(6, "attr1_6", 6, "attr3_6", 6L, "attr5_6", "attr6_6");
        save(uKeyElementMapper, ukElement1, ukElement2, ukElement3, ukElement4, ukElement5, ukElement6);

        UniqueKey uKey;

        //测试通过group创建UKey后查询为空的情况
        uKey = UniqueKey.of("a", "1", "2", "3");
        var uKeyElement = uKeyElementMapper.get(uKey);
        Assert.assertNull(uKeyElement);

        //测试通过group创建UKey后的正常查询
        uKey = UniqueKey.of("a", "attr1_1", 1, 1L);
        uKeyElement = uKeyElementMapper.get(uKey);
        Assert.assertNotNull(uKeyElement);
    }

    @DataProvider
    public Object[][] p4testGetByUkeyFail() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 1;
            Object[] objects;

            //组个数为3, 现在只有1个, 不能调用
            objects = new Object[paramLength];
            objects[0] = UniqueKey.of("a", 1);
            objectsList.add(objects);

            //两个也不能调用
            objects = new Object[paramLength];
            objects[0] = UniqueKey.of("a", 1, "attr4_1");
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证调用uniqueKey失败的情况 */
    @Test(dataProvider = "p4testGetByUkeyFail", expectedExceptions = MybatisException.class)
    public void testGetByUkeyFail(UniqueKey uniqueKey) {
        uKeyElementMapper.get(uniqueKey);
    }

    /** 验证使用keyGenerator的情况 */
    @Test
    public void testSaveWithKeyGenerator() {
        //原值为空null 属性为attr5
        val ukElement8 = new UKeyElement(8, "attr1_8", 8, "attr3_8", 0L, null, "att6_8");
        uKeyElementMapper.save(ukElement8);
        var obj = uKeyElementMapper.get(Key.of(8));
        Assert.assertNotNull(obj.getAttr5());

        //原值为数字0 属性为attr4
        val ukElement7 = new UKeyElement(7, "attr1_7", 7, "attr3_7", 0L, "attr5_7", "attr6_7");
        uKeyElementMapper.save(ukElement7);
        obj = uKeyElementMapper.get(Key.of(7));
        Assert.assertNotEquals(obj.getAttr4(), 0L);
    }

    /** 验证调整结果类型的场景 */
    @Test
    public void testGetByResultType() {
        val ukeyEle1 = new UKeyElement(-8, "attr1_8", 8, "attr3_8", 8L, "attr5_8", "attr6_8");
        save(uKeyElementMapper, ukeyEle1);

        val entityInfo = EntityInfoHolder.get(UKeyElement.class);
        val ukey = ColumnInfoUtils.getUniqueKey(entityInfo, "a", ukeyEle1);

        Object obj = uKeyElementMapper.get(ukey, UkeySubElement.class);
        //非空
        Assert.assertNotNull(obj);
        //强转处理
        UkeySubElement ele = (UkeySubElement) obj;
        //主键相同
        Assert.assertEquals(ele.getId(), -8);
    }

    @Test
    public void testUpdateWithUkExample() {
        val item = new UKeyElement(-9, "attr1_9", 9, "attr3_9", 9L, "attr5_9", "attr6_9");
        save(uKeyElementMapper, item);

        val updateV = CloneUtils.clone(item);
        //清掉主键信息
        updateV.setId(0);
        Assert.assertTrue(!updateV.key().fullfill());

        updateV.setAttr6("abcd");
        //使用组名b,即根据属性 attr3 来进行更新
        Example<UKeyElement> example = Example.of(updateV).usingUk("b").must("attr6");
        uKeyElementMapper.updateByExample(example);

        val selectV1 = uKeyElementMapper.get(UniqueKey.of("b", item.getAttr3()));
        //查询出来的数据与原来相一致
        Assert.assertEquals(selectV1.getId(), item.getId());
        //查询出来的值应该被修改了，且与修改值相同
        Assert.assertEquals(selectV1.getAttr6(), updateV.getAttr6());
    }

}
