package io.iflym.mybatis.mapperx.generator;

import io.iflym.mybatis.domain.KeyGenerator;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mapperx.domain.UKeyElement;

/**
 * 用于随机生成一个字符串key值
 * created at 2018-01-08
 *
 * @author flym
 */
public class MyStrKeyGenerator implements KeyGenerator<UKeyElement, String> {
    @Override
    public String generateKey(Mapper<UKeyElement> mapper, UKeyElement entity, ColumnInfo columnInfo) {
        return String.valueOf(Math.random());
    }
}
