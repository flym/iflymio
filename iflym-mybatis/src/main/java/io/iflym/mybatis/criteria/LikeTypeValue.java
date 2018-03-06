package io.iflym.mybatis.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * 用于描述like中的匹配规则
 *
 * @author flym
 * Created by flym on 6/3/2016.
 */
@Getter
@AllArgsConstructor
public enum LikeTypeValue {
    /** 两边没有 %号, 相当于 = */
    EXACT("相等"),
    /** 后面加 % */
    START("前面匹配"),
    /** 前面加 % */
    END("后面匹配"),
    /** 两边都有 % */
    ANY("中间匹配");

    /** 用于处理在like表达式中的特殊字符 */
    private static final Pattern LIKE_SPECIAL_PATTERN = Pattern.compile("([%_])");

    /** 描述信息 */
    private String desc;

    /** 根据匹配规则追加特定的%号 */
    public String wrap(String value) {
        //在like中的 %和_需要特殊进行处理
        value = LIKE_SPECIAL_PATTERN.matcher(value).replaceAll("\\\\$1");
        switch(this) {
            case EXACT:
                return value;
            case ANY:
                return "%" + value + "%";
            case START:
                return value + "%";
            case END:
                return "%" + value;
            default:
                throw new RuntimeException("不支持的匹配模式:" + this);
        }
    }
}
