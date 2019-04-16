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
@EqualsAndHashCode(of = {"name", "title"})
@ToString
public class To1 {
    String name;

    String title;

    public static To1 build(String name, String title) {
        val v = new To1();
        v.name = name;
        v.title = title;

        return v;
    }
}
