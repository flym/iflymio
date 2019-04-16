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
@EqualsAndHashCode(of = {"name", "title", "extraDesc"})
@ToString
public class To2 {
    String name;

    String title;

    @MapField.From(clazz = From2.class, property = "desc")
    String extraDesc;

    public static To2 build(String name, String title, String extraDesc) {
        val v = new To2();
        v.name = name;
        v.title = title;
        v.extraDesc = extraDesc;

        return v;
    }
}
