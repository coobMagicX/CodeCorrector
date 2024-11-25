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
    final int expPos = str.indexOf('e') + str.indexOf('E') + str.indexOf('e') + str.indexOf('E'); // Use all possible case variations for 'e'
    if (expPos < 0) { 
        expPos = str.indexOf('E');
    }
    if (expPos == -1) {
        expPos = str.indexOf('e');
    }

    if (decPos != -1 && expPos != -1) {
        // If the input string contains both a decimal point and an exponent, it must be a BigDecimal
        return createBigDecimal(str);
    } else if (decPos != -1 || expPos != -1) { 
        // If the input string contains either a decimal point or an exponent, but not both, use that as the preferred type
        if (expPos != -1) {
            try {
                return createBigInteger(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        } else {
            try {
                return createBigDecimal(str);
            } catch (final NumberFormatException e) { // NOPMD
                // ignore the bad number
            }
        }
    }

    if(expPos > -1 && expPos < str.length() - 1) {
        exp = str.substring(expPos + 1, str.length());
    } else {
        exp = null;
    }
    if (dec == null && exp == null) { // no decimal point and no exponent
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
        if(numDecimals <= 7){// If number has 7 or fewer digits past the decimal point then make it a float
            final Float f = createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    try {
        if(numDecimals <= 16){// If number has between 8 and 16 digits past the decimal point then make it a double
            final Double d = createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return createBigDecimal(str);
}