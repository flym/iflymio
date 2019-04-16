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
public class FromMix1 {
    String name;

    String title;

    String fromDesc;

    public static FromMix1 build(String name, String title, String fromDesc) {
        val v = new FromMix1();
        v.name = name;
        v.title = title;
        v.fromDesc = fromDesc;

        return v;
    }
}
