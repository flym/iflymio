package io.iflym.mybatis.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于描述关联的方式
 *
 * @author flym
 * Created by flym on 6/3/2016.
 */
@AllArgsConstructor
@Getter
public enum JoinTypeValue {
    /** 默认的join方式 */
    INNER("内联接"),
    /** left join方式 */
    LEFT("左联接"),
    /** right join方式 */
    RIGHT("右联接");

    /** 描述信息 */
    private String desc;

    /** 相应的sql表达 */
    public String toSql() {
        switch(this) {
            case INNER:
                return " inner join ";
            case LEFT:
                return " left join ";
            case RIGHT:
                return " right join ";
            default:
                throw new RuntimeException("不支持的联接方式" + this);
        }
    }
}
