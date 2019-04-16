package io.iflym.core.converter.generator.converterhelper;

import io.iflym.core.converter.MapField;
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
@EqualsAndHashCode(of = {"name", "title", "desc1", "desc2"})
public class To7 {
    String name;

    String title;

    String desc1;

    @MapField.From(clazz = From7.class, property = "fromDesc2")
    String desc2;

    public static To7 build(String name, String title, String desc1, String desc2) {
        val v = new To7();
        v.name = name;
        v.title = title;
        v.desc1 = desc1;
        v.desc2 = desc2;

        return v;
    }
}
