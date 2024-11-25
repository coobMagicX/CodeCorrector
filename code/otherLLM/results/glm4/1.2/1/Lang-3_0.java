import org.apache.commons.lang3.StringUtils;

public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null || StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank or null string is not a valid number");
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
    if (expPos > -1 && expPos < str.length()) {
        exp = str.substring(expPos + 1);
        if (dec != null) {
            dec = str.substring(decPos + 1, expPos);
            mant = str.substring(0, decPos);
        } else {
            mant = str.substring(0, expPos);
            dec = null;
        }
    } else {
        if (decPos > -1) {
            dec = str.substring(decPos + 1);
            mant = str.substring(0, decPos);
        } else {
            mant = str;
            dec = null;
            exp = null;
        }
    }

    if (lastChar == 'f' || lastChar == 'F') {
        str = str.substring(0, str.length() - 1);
        try {
            final Float f = NumberUtils.createFloat(mant);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !isAllZeros(dec)))) {
                return f;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    } else if (lastChar == 'd' || lastChar == 'D') {
        str = str.substring(0, str.length() - 1);
        try {
            final Double d = NumberUtils.createDouble(mant);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !isAllZeros(dec)))) {
                return d;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (dec == null && exp == null) { // no decimal point and no exponent
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

    // Must be a Float, Double, BigDecimal
    try {
            final Float f = createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !isAllZeros(dec)))) {
                return f;
            }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    try {
            final Double d = createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !isAllZeros(dec)))) {
                return d;
            }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return createBigDecimal(str);
}

private static boolean isAllZeros(String str) {
    for (char c : str.toCharArray()) {
        if (!Character.isDigit(c)) return false;
    }
    return true;
}