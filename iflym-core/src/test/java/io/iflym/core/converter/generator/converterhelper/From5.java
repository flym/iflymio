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
@Mapping(to = To5.class, fields = {
        @Mapping.Field(from = "extraDesc", to = "desc")
})
public class From5 {
    String name;

    String title;

    String extraDesc;

    public static From5 build(String name, String title, String extraDesc) {
        val v = new From5();
        v.name = name;
        v.title = title;
        v.extraDesc = extraDesc;

        return v;
    }
}
