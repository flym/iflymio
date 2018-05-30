package io.iflym.mybatis.mapperx.domain;

import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.annotation.Column;
import io.iflym.mybatis.domain.annotation.Id;
import io.iflym.mybatis.domain.annotation.Table;
import io.iflym.mybatis.domain.field.json.Jsoned;
import io.iflym.mybatis.mapperx.vo.User;
import lombok.*;

import java.util.List;

/**
 * 测试表item
 * Created by flym on 2017/11/8.
 */
@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_jsoned_item")
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class JsonedItem implements Entity<JsonedItem> {
    public static final String TABLE_NAME = "t_jsoned_item";

    public static final String TABLE_DDL = "CREATE TABLE `t_jsoned_item` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `str_value` varchar(1024) DEFAULT NULL,\n" +
            "  `user_value` varchar(1024) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") DEFAULT CHARSET=utf8;";
    @Id
    @Column
    private long id;

    @Column
    private Jsoned<String> strValue;

    @Column
    private Jsoned<List<User>> userValue;

    @Override
    public Key key() {
        return Key.of(id);
    }

}
