package io.iflym.mybatis.mapperx.domain;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.Table;
import io.iflym.mybatis.domain.annotation.UniqueId;
import io.iflym.mybatis.mapperx.generator.MyKeyGenerator;
import io.iflym.mybatis.mapperx.generator.MyStrKeyGenerator;
import lombok.*;

/**
 * Created by luyi on 2017/12/11.
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "t_element_ukey")
public class UKeyElement implements Entity<UKeyElement> {
    public static final String TABLE_NAME = "t_element_ukey";

    public static final String TABLE_DDL = "CREATE TABLE `t_element_ukey` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `attr1` varchar(255) DEFAULT NULL,\n" +
            "  `attr2` int(11) DEFAULT NULL,\n" +
            "  `attr3` varchar(255) DEFAULT NULL,\n" +
            "  `attr4` bigint(11) DEFAULT NULL,\n" +
            "  `attr5` varchar(255) DEFAULT NULL,\n" +
            "  `attr6` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") DEFAULT CHARSET=utf8;";
    @Id
    @Column
    private long id;

    @Column
    @UniqueId(group = "a")
    private String attr1;

    @Column
    @UniqueId(group = "a", order = 1)
    private int attr2;

    @Column
    @UniqueId(group = "b", order = 2)
    private String attr3;

    @Column
    @UniqueId(group = "a", keyGenerator = MyKeyGenerator.class)
    private long attr4;

    @Column
    @UniqueId(keyGenerator = MyStrKeyGenerator.class)
    private String attr5;

    @Column
    @UniqueId
    private String attr6;

    @Override
    public Key key() {
        return Key.of(id);
    }
}
