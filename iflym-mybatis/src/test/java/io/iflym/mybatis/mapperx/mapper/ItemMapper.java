package io.iflym.mybatis.mapperx.mapper;

import io.iflym.mybatis.domain.Page;
import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mapperx.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by flym on 2017/11/8. */
@Repository
public interface ItemMapper extends Mapper<Item> {
    Item getByUsername(String username);

    List<Item> queryByIdOrUsernameEqOrderByAgeDesc(long id, String username);

    List<Item> listByAgeGt(int age, Page page);

    int countByUsername(String username);

    Integer countById(long id);
}
