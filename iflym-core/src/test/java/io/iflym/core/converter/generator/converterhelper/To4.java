package io.iflym.core.converter.generator.converterhelper;

import io.iflym.core.converter.Mapping;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * created at 2019-04-16
 *
 * @author flym
 */
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"name", "title", "desc"})
@Mapping(from = From4.class, fields = {@Mapping.Field(from = "extraDesc", to = "desc")})
@ToString
public class To4 {
    String name;

    String title;

    String desc;

    public static To4 build(String name, String title, String desc) {
        val v = new To4();
        v.name = name;
        v.title = title;
        v.desc = desc;

        return v;
    }
}
