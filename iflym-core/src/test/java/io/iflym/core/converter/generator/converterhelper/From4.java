package io.iflym.core.converter.generator.converterhelper;

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
public class From4 {
    String name;

    String title;

    String extraDesc;

    public static From4 build(String name, String title, String extraDesc) {
        val v = new From4();
        v.name = name;
        v.title = title;
        v.extraDesc = extraDesc;

        return v;
    }
}
