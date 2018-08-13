package io.iflym.core.util.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class String2BigIntegerConverter implements Converter<String, BigInteger> {
    public static final String2BigIntegerConverter INSTANCE = new String2BigIntegerConverter();

    @Override
    public BigInteger apply(String s) {
        return new BigDecimal(s).toBigInteger();
    }
}
