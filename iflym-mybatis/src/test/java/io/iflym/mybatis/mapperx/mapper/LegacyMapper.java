package io.iflym.mybatis.mapperx.mapper;

import io.iflym.mybatis.mapperx.domain.Item;
import org.apache.ibatis.annotations.Param;

/**
 * 验证一个传统的mapper能够正常工作,即没有继承于mapper接口的
 * Created by flym on 2017/12/15.
 */
public interface LegacyMapper {
    Item getById(@Param("id") long id);
}
