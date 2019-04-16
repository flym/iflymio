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
public class FromMix2 {
    String name;

    String title;

    String fromDesc1;

    @MapField.To(clazz = ToMix2.class, property = "toDesc2")
    String fromDesc2;

    String fromDesc3;

    public static FromMix2 build(String name, String title, String fromDesc1, String fromDesc2, String fromDesc3) {
        val v = new FromMix2();
        v.name = name;
        v.title = title;
        v.fromDesc1 = fromDesc1;
        v.fromDesc2 = fromDesc2;
        v.fromDesc3 = fromDesc3;

        return v;
    }
}
