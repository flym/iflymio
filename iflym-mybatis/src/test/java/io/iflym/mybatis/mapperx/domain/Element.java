package io.iflym.mybatis.mapperx.domain;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.DeleteTag;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.Table;
import lombok.*;

/**
 * Created by luyi on 2017/12/6.
 */
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_element")
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
public class Element implements Entity<Element> {

    public static final String TABLE_NAME = "t_element";

    public static final String TABLE_DDL = "CREATE TABLE `t_element` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(255) DEFAULT NULL,\n" +
            "  `is_active` int(11) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") DEFAULT CHARSET=utf8;";
    @Id
    @Column
    private long id;

    @Column
    private String name;

    @DeleteTag(value = "0")
    @Column
    private int isActive;

    @Override
    public Key key() {
        return Key.of(id);
    }
}
