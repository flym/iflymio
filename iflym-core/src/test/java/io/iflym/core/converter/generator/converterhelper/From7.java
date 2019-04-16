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
public class From7 {
    String name;

    String title;

    @MapField.To(clazz = To7.class, property = "desc1")
    String fromDesc1;

    String fromDesc2;

    public static From7 build(String name, String title, String fromDesc1, String fromDesc2) {
        val v = new From7();
        v.name = name;
        v.title = title;
        v.fromDesc1 = fromDesc1;
        v.fromDesc2 = fromDesc2;

        return v;
    }
}
