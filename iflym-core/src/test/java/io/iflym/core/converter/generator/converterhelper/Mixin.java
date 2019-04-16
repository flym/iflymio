package io.iflym.core.converter.generator.converterhelper;

import io.iflym.core.converter.MapMixin;
import io.iflym.core.converter.Mapping;

/**
 * created at 2019-04-16
 *
 * @author flym
 */
@MapMixin
@Mapping(from = FromMix1.class, to = ToMix1.class, fields = {
        @Mapping.Field(from = "fromDesc", to = "toDesc")
})
@Mapping(from = FromMix2.class, to = ToMix2.class, fields = {
        @Mapping.Field(from = "fromDesc1", to = "toDesc1")
})
public class Mixin {
}
