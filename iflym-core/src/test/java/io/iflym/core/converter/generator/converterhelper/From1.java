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
public class From1 {
    String name;

    String title;

    public static From1 build(String name, String title) {
        val v = new From1();
        v.name = name;
        v.title = title;

        return v;
    }
}
