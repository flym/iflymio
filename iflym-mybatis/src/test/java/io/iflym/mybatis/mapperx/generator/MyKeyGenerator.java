package io.iflym.mybatis.mapperx.generator;

import io.iflym.mybatis.domain.KeyGenerator;
import io.iflym.mybatis.domain.info.ColumnInfo;
import io.iflym.mybatis.mapperx.Mapper;
import io.iflym.mybatis.mapperx.domain.UKeyElement;

import java.util.Random;

/**
 * @author luyi
 */
public class MyKeyGenerator implements KeyGenerator<UKeyElement, Long> {

    @Override
    public Long generateKey(Mapper<UKeyElement> mapper, UKeyElement element, ColumnInfo columnInfo) {
        return (long) (new Random().nextDouble() * 1000000);
    }

}
