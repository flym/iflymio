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
@EqualsAndHashCode(of = {"name", "title", "toDesc1", "toDesc2", "toDesc3"})
@ToString
public class ToMix2 {
    String name;

    String title;

    String toDesc1;

    String toDesc2;

    @MapField.From(clazz = FromMix2.class, property = "fromDesc3")
    String toDesc3;

    public static ToMix2 build(String name, String title, String toDesc1, String toDesc2, String toDesc3) {
        val v = new ToMix2();
        v.name = name;
        v.title = title;
        v.toDesc1 = toDesc1;
        v.toDesc2 = toDesc2;
        v.toDesc3 = toDesc3;

        return v;
    }
}
