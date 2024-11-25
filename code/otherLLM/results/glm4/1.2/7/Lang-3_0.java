import java.math.BigDecimal;

public static BigDecimal createNumber(final String str) throws NumberFormatException {
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
        return new BigDecimal(createInteger(str));
    }

    final char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    final int decPos = str.indexOf('.');
    final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point
        if (expPos > -1) { // there is an exponent
            if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
    }

    if (dec == null && exp == null) { // no decimal point and no exponent
        // Must be an Integer, Long, Biginteger or BigDecimal
        try {
            return new BigDecimal(createInteger(str));
        } catch (final NumberFormatException nfe) {
            // ignore the bad number
        }
        try {
            return new BigDecimal(createLong(str));
        } catch (final NumberFormatException nfe) {
            // ignore the bad number
        }
        return new BigDecimal(createBigInteger(str));
    }

    // Check for float, double and convert to BigDecimal if necessary
    final boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));

    try {
        final Float f = createFloat(str);
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
            return new BigDecimal(f.toString());
        }
    } catch (final NumberFormatException nfe) {
        // ignore the bad number
    }

    try {
        final Double d = createDouble(str);
        if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
            return new BigDecimal(d.toString());
        }
    } catch (final NumberFormatException nfe) {
        // ignore the bad number
    }

    return new BigDecimal(str);
}