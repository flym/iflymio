package io.iflym.core.util.converter;

import java.math.BigDecimal;

public class String2BigDecimalConverter implements Converter<String, BigDecimal> {
    public static final String2BigDecimalConverter INSTANCE = new String2BigDecimalConverter();

    @Override
    public BigDecimal apply(String s) {
        return new BigDecimal(s);
    }


}
