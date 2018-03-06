package io.iflym.mybatis.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.iflym.mybatis.domain.util.AttrUtils;

import java.util.Map;

/**
 * 用于描述可以被设置某些本身并不在当前对象上的属性,通过扩展属性的方式将相应的属性附在当前对象上
 * 同时,此类支持json(jackson & fastjson)的序列化和反序列化,即在对外输出时,将相应的属性输出为与当前对象同一级属性集中,
 * 并在反序列化时也支持将并不在对象上的属性输入到此属性集中
 * <p>
 *
 * @author flym
 * Created by flym on 2017/8/29.
 */
public interface Attribute {
    /**
     * 获取相应的属性信息
     *
     * @return 所有的设置属性值
     */
    @JsonAnyGetter
    default Map<String, Object> attr() {
        return AttrUtils.attr(this);
    }

    /**
     * 设置单个属性
     *
     * @param key   属性值的key
     * @param value 具体的属性值
     */
    @JsonAnySetter
    default <V_> void attr(String key, V_ value) {
        AttrUtils.attr(this, key, value);
    }
}
