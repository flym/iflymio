package io.iflym.core.util.converter;

import java.math.BigInteger;

public class BigInteger2StringConverter implements Converter<BigInteger, String> {
    public static final BigInteger2StringConverter INSTANCE = new BigInteger2StringConverter();

    @Override
    public String apply(BigInteger s) {
        return s.toString();
    }
}
