package io.iflym.mybatis.mapperx.mapper;

import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mapperx.domain.Element;
import org.springframework.stereotype.Repository;

/**
 * Created by luyi on 2017/12/6.
 */
@Repository
public interface ElementMapper extends Mapper<Element> {
    Element getByName(String name);
}
