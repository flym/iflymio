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
@EqualsAndHashCode(of = {"name", "title", "desc"})
@ToString
public class To3 {
    String name;

    String title;

    String desc;

    public static To3 build(String name, String title, String desc) {
        val v = new To3();
        v.name = name;
        v.title = title;
        v.desc = desc;

        return v;
    }
}
