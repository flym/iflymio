package io.iflym.core.converter.generator.converterhelper;

import io.iflym.core.converter.MapField;
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
@ToString
@EqualsAndHashCode(of = {"name", "title", "desc"})
@Mapping(from = From6.class, fields = {
        @Mapping.Field(from = "desc2", to = "desc")
})
public class To6 {
    String name;

    String title;

    @MapField.From(clazz = From6.class, property = "desc1")
    String desc;

    public static To6 build(String name, String title, String desc) {
        val v = new To6();
        v.name = name;
        v.title = title;
        v.desc = desc;

        return v;
    }
}
