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
    for (final String pfx : hex_prefixes) {
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
    final int expPos = str.indexOf('e') + str.indexOf('E');
    if (expPos == -1) {
        expPos = str.length();
    } else {
        exp = str.substring(expPos + 1);
    }

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point
        dec = str.substring(decPos + 1, expPos);
        mant = str.substring(0, decPos);
        numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.
    } else {
        mant = str;
        dec = null;
    }

    if (dec == null && exp == null) { // no decimal point and no exponent
        try {
            return createInteger(mant);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createLong(mant);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(mant);
    }

    // Must be a Float, Double, BigDecimal
    final boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
    try {
        if (!allZeros) {
            final Double d = NumberUtils.createDouble(mant + (exp != null ? "E" + exp : ""));
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final Float f = NumberUtils.createFloat(mant + (exp != null ? "E" + exp : ""));
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
            return f;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return createBigDecimal(mant + (exp != null ? "E" + exp : ""));
}