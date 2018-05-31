package io.iflym.mybatis.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.iflym.BaseTest;
import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.criteria.Criterion;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.field.json.Jsoned;
import io.iflym.mybatis.domain.field.json.JsonedMapperFactory;
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
        //注册mapper
        JsonedMapperFactory.INSTANCE.registerDefaultJacksonJsonedMapper(new ObjectMapper());
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
}
