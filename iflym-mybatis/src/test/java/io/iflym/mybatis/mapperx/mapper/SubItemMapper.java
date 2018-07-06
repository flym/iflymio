package io.iflym.mybatis.mapperx.mapper;

import io.iflym.mybatis.mapperx.domain.Item;
import org.apache.ibatis.annotations.Param;

/**
 * created at 2018-07-06
 *
 * @author flym
 */
public interface SubItemMapper extends ItemMapper {
    Item queryWithUsername(@Param("username") String username);

    Item queryWithUsernameUseDefaultMap(@Param("username") String username);
}
