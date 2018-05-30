package io.iflym.mybatis.mapperx.vo;

import lombok.*;

/**
 * 使用的vo类，用于验证jsoned值
 * created at 2018-05-30
 *
 * @author flym
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class User {
    private String username;
    private String password;
}
