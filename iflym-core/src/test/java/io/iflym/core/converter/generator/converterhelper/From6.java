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
public class From6 {
    String name;

    String title;

    String desc1;

    String desc2;

    public static From6 build(String name, String title, String desc1, String desc2) {
        val v = new From6();
        v.name = name;
        v.title = title;
        v.desc1 = desc1;
        v.desc2 = desc2;

        return v;
    }
}
