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
    if (decPos > -1) {
        final int decimalLength = str.length() - decPos;
        if (decimalLength < 7 || decimalLength > 15) { // adjusted range for double
            try {
                return NumberUtils.createDouble(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        } else {
            numDecimals = decimalLength;
        }
    }

    if (decPos > -1 && expPos > -1) {
        final int exponentOffset = str.length() - expPos;
        if (exponentOffset < 0 || exponentOffset >= numDecimals + 7 || exponentOffset <= numDecimals - 15) { // adjusted range for double
            try {
                return NumberUtils.createDouble(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        }
    }

    if (decPos > -1 && expPos == -1) {
        final int decimalLength = str.length() - decPos;
        if (decimalLength < 7 || decimalLength > 15) { // adjusted range for double
            try {
                return NumberUtils.createDouble(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        }
    }

    if (decPos == -1 && expPos > -1) {
        final int exponentOffset = str.length() - expPos;
        if (exponentOffset < 0 || exponentOffset >= 7 || exponentOffset <= -15) { // adjusted range for double
            try {
                return NumberUtils.createDouble(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        }
    }

    if (decPos > -1 && expPos == -1) {
        final int decimalLength = str.length() - decPos;
        if (decimalLength < 7 || decimalLength > 15) { // adjusted range for double
            try {
                return NumberUtils.createDouble(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        }
    }

    if (decPos == -1 && expPos == -1) { // no decimal point and no exponent
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

    //Must be a Float, Double, BigDecimal
    final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
    try {
            final Float f = NumberUtils.createFloat(str);
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

    return createBigDecimal(str);
}