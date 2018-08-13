package io.iflym.core.util.converter;

import java.math.BigDecimal;

public class BigDecimal2StringConverter implements Converter<BigDecimal, String> {
    public static final BigDecimal2StringConverter INSTANCE = new BigDecimal2StringConverter();

    @Override
    public String apply(BigDecimal s) {
        return s.toPlainString();
    }
}
