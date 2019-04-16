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
@EqualsAndHashCode(of = {"name", "title", "toDesc"})
@ToString
public class ToMix1 {
    String name;

    String title;

    String toDesc;

    public static ToMix1 build(String name, String title, String toDesc) {
        val v = new ToMix1();
        v.name = name;
        v.title = title;
        v.toDesc = toDesc;

        return v;
    }
}
