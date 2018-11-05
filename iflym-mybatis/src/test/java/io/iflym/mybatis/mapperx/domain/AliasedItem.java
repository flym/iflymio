package io.iflym.mybatis.mapperx.domain;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.Table;
import lombok.*;

/**
 * created at 2018-11-05
 *
 * @author flym
 */
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_aliased_item")
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "username"})
@ToString(of = {"id", "username"})
public class AliasedItem implements Entity<AliasedItem> {
    public static final String TABLE_NAME = "t_aliased_item";

    public static final String TABLE_DDL = "CREATE TABLE `t_aliased_item` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `username` varchar(255) DEFAULT NULL,\n" +
            "  `desc` varchar(255) DEFAULT NULL,\n" +
            "  `order` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") DEFAULT CHARSET=utf8;";
    @Id
    @Column
    private long id;

    @Column
    private String username;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "`order`")
    private String order;

    @Override
    public Key key() {
        return Key.of(id);
    }
}
