package io.iflym.mybatis.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.iflym.BaseTest;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.field.json.Jsoned;
import io.iflym.mybatis.domain.field.json.jackson.JsonedModule;
import io.iflym.mybatis.mapperx.AssertExt;
import io.iflym.mybatis.mapperx.domain.JsonedItem;
import io.iflym.mybatis.mapperx.mapper.JsonedItemMapper;
import io.iflym.mybatis.mapperx.vo.User;
import io.iflym.mybatis.util.DbUtils;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 验证jsoned值是否可成功写入和处理
 * created at 2018-05-30
 *
 * @author flym
 */
public class JsonedTest extends BaseTest {
    @Resource
    private JsonedItemMapper jsonedItemMapper;

    @BeforeClass
    private void prepareTable() throws Exception {
        DbUtils.createTable(jdbcTemplate, null, JsonedItem.TABLE_NAME, JsonedItem.TABLE_DDL);
    }

    @Test
    public void testJsoned() {
        val id = -1L;
        val str = "中文验证test1";
        val userList1 = Lists.newArrayList(new User("用户1", "密码1"), new User("用户2", "密码2"));
        val userList2 = Lists.newArrayList(new User("用户1-2", "密码1-2"), new User("用户2-2", "密码2-2"));

        val item = new JsonedItem(id, new Jsoned<>(str), new Jsoned<>(userList1));
        jsonedItemMapper.save(item);

        //验证相应的获取出来的数据与预期一致
        val item2 = jsonedItemMapper.get(Key.of(id));
        Assert.assertEquals(item2.getStrValue().t1, str);
        Assert.assertEquals(item2.getUserValue().t1, userList1);

        //验证修改能成功
        item2.upmark();
        item2.setUserValue(new Jsoned<>(userList2));
        jsonedItemMapper.update(item2);

        val item3 = jsonedItemMapper.get(Key.of(id));
        Assert.assertEquals(item3.getUserValue().t1, userList2);

        //验证使用criteria查询能够正常查询

        Criteria<JsonedItem> criteria = Criteria.of(JsonedItem.class);
        criteria.where(Criterion.eq("strValue", new Jsoned<>(str)));
        List<JsonedItem> list = jsonedItemMapper.listCriteria(criteria);

        //能查询出一条
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getStrValue().t1, str);
    }

    /** 验证序列化和反序列化均能正常工作 */
    @Test
    public void testJacksonSerializerAndDeserializer() throws Exception {
        val objectMapper = new ObjectMapper();
        JsonedModule.INSTANCE.registerJackson(objectMapper);

        val strV = "中文验证";

        val item = new JsonedItem(1, new Jsoned<>(strV), null);
        val str = objectMapper.writeValueAsString(item);

        //要求数据中不能存在 t1的字符串
        Assert.assertTrue(!str.contains("\"t1\""), "序列化数据不正确:" + str);
        Assert.assertTrue(str.contains("\"strValue\""), "序列化数据不正确:" + str);

        val item2 = objectMapper.readValue(str, JsonedItem.class);
        Assert.assertNotNull(item2.getStrValue());
        Assert.assertEquals(item2.getStrValue().t1, item.getStrValue().t1);

        val user1 = new User("用户1", "密码1");
        val user2 = new User("用户1-2", "密码1-2");
        val list = Lists.newArrayList(user1, user2);
        val bitem = new JsonedItem(2, new Jsoned<>(strV), new Jsoned<>(list));

        val bstr = objectMapper.writeValueAsString(bitem);
        //要求数据中不能存在 t1的字符串
        Assert.assertTrue(!str.contains("\"t1\""), "序列化数据不正确:" + str);
        Assert.assertTrue(str.contains("\"strValue\""), "序列化数据不正确:" + str);
        Assert.assertTrue(str.contains("\"userValue\""), "序列化数据不正确:" + str);

        val bitem2 = objectMapper.readValue(bstr, JsonedItem.class);
        Assert.assertNotNull(bitem2.getStrValue());
        Assert.assertNotNull(bitem2.getUserValue());
        AssertExt.assertContentEquals(bitem2.getUserValue().t1, bitem.getUserValue().t1, Object::equals);

        //验证对null的处理
        val citem = new JsonedItem(3, new Jsoned<>(null), null);
        val cstr = objectMapper.writeValueAsString(citem);
        val citem2 = objectMapper.readValue(cstr, JsonedItem.class);
        Assert.assertNull(citem2.getStrValue());
    }
}
