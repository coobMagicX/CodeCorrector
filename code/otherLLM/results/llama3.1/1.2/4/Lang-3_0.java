public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    // Need to deal with all possible hex prefixes here
    final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
    int pfxLen = 0;
    for(final String pfx : hex_prefixes) {
        if (str.startsWith(pfx)) {
            pfxLen += pfx.length();
            break;
        }
    }
    if (pfxLen > 0) { // we have a hex number
        final int hexDigits = str.length() - pfxLen;
        if (hexDigits > 16) { // too many for Long
            return createBigInteger(str);
        }
        if (hexDigits > 8) { // too many for an int
            return createLong(str);
        }
        return createInteger(str);
    }
    final char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    final int decPos = str.indexOf('.');
    final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
    // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
    // and the parsing which will detect if e or E appear in a number due to using the wrong offset

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point

        if (expPos > -1) { // there is an exponent
            if (expPos < decPos || expPos >= str.length()) {
                throw new NumberFormatException("Invalid exponent position");
            }
            if (str.charAt(expPos) == 'e' || str.charAt(expPos) == 'E') {
                if (expPos + 1 >= str.length() || !Character.isDigit(str.charAt(expPos + 1))) {
                    throw new NumberFormatException("Invalid exponent value");
                }
                exp = str.substring(expPos + 1);
            } else {
                throw new NumberFormatException("Invalid exponent character");
            }
        } else {
            if (decPos == -1) { // no decimal point, so it must be an integer
                return createInteger(str);
            }
            exp = null;
        }

        if (decPos > 0 && decPos < str.length()) {
            dec = str.substring(decPos);
        } else {
            throw new NumberFormatException("Invalid decimal position");
        }

        mant = str.substring(0, decPos);

    } else { // no decimal point
        exp = null;
        if (str.charAt(str.length() - 1) == 'e' || str.charAt(str.length() - 1) == 'E') {
            throw new NumberFormatException("Invalid exponent character");
        }
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            try {
                return createLong(str);
            } catch (final NumberFormatException e) { // NOPMD
                return createBigInteger(str);
            }
        }
    }

    if (exp == null && dec == null) { // no decimal point and no exponent
        //Must be an Integer, Long, Biginteger
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createLong(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(str);
    }

    final boolean allZeros = isAllZeros(mant) && exp == null;
    if (allZeros) {
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createLong(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    try {
        final Float f = createFloat(str);
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
            return f;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    try {
        final Double d = createDouble(str);
        if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
            return d;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final BigDecimal bigDecimal = createBigDecimal(str);
        if (!(bigDecimal.isInfinite() || (bigDecimal.floatValue() == 0.0F && !allZeros))) {
            return bigDecimal;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    throw new NumberFormatException("Invalid input string");
}