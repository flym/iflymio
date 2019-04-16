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
public class From2 {
    String name;

    String title;

    String desc;

    public static From2 build(String name, String title, String desc) {
        val v = new From2();
        v.name = name;
        v.title = title;
        v.desc = desc;

        return v;
    }
}
