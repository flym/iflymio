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
public class From3 {
    String name;

    String title;

    @MapField.To(clazz = To3.class, property = "desc")
    String extraDesc;

    public static From3 build(String name, String title, String extraDesc) {
        val v = new From3();
        v.name = name;
        v.title = title;
        v.extraDesc = extraDesc;

        return v;
    }
}
