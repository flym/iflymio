package io.iflym.mybatis.mapperx.mapper.mappera;

import io.iflym.mybatis.mapperx.domain.Item;
import io.iflym.mybatis.mapperx.mapper.ItemMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * created at 2018-07-11
 *
 * @author flym
 */
@Repository("mappera.SubXItemMapper")
public interface SubXItemMapper extends ItemMapper {
    Item queryWithUsername(@Param("username") String username);
}
