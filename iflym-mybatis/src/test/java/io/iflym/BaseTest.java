package io.iflym;

import io.iflym.mybatis.LtwListener;
import io.iflym.mybatis.SpringConfiguration;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.mapperx.Mapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

import java.util.List;

/** Created by flym on 2017/11/8. */
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = SpringConfiguration.class, initializers = LtwListener.class)
public class BaseTest extends AbstractTransactionalTestNGSpringContextTests {

    /** 工具方法,保存对象 */
    @SuppressWarnings("unchecked")
    protected void save(Mapper mapper, Entity... entities) {
        for(Entity e : entities) {
            mapper.save(e);
        }
    }

    /** 工具方法,保存对象 */
    @SuppressWarnings("unchecked")
    protected void save(Mapper mapper, List<? extends Entity> entityList) {
        entityList.forEach(mapper::save);
    }
}
