package io.iflym.mybatis.domain;

/**
 * 以支持主键以及索引级的共同功能
 * Created by flym on 2017/12/25.
 *
 * @author flym
 */
public interface Keyed {
    /**
     * 长度值
     * 即这个主键由多少个字段组成,一般情况下均由一个long型字段组成,这种情况下其长度为1
     * 如果是聚合主键,则长度值则由组合这个聚合主键的多个字段组合,长度值即字段的个数
     *
     * @return 主键字段的个数
     */
    int length();

    /**
     * 获取在指定位置的值
     *
     * @param i 相应的位置,从0开始
     * @return 此位置上的值
     */
    <V> V index(int i);
}
