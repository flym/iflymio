package io.iflym.mybatis.mapperx.domain;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.DeleteTag;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.Table;
import lombok.*;

/**
 * 测试表item
 * Created by flym on 2017/11/8.
 */
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_item")
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "username"})
@ToString(of = {"id", "username"})
public class Item implements Entity<Item> {
    public static final String TABLE_NAME = "t_item";

    public static final String TABLE_DDL = "CREATE TABLE `t_item` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `username` varchar(255) DEFAULT NULL,\n" +
            "  `age` int(11) DEFAULT NULL,\n" +
            "  `is_active` int(11) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") DEFAULT CHARSET=utf8;";
    @Id
    @Column
    private long id;

    @Column
    private String username;

    @Column
    private int age;

    @Column
    private int isActive;

    @Override
    public Key key() {
        return Key.of(id);
    }

}
