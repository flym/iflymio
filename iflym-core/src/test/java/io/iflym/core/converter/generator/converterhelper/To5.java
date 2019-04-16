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
@ToString
@EqualsAndHashCode(of = {"name", "title", "desc"})
public class To5 {
    String name;

    String title;

    String desc;

    public static To5 build(String name, String title, String desc) {
        val v = new To5();
        v.name = name;
        v.title = title;
        v.desc = desc;

        return v;
    }
}
