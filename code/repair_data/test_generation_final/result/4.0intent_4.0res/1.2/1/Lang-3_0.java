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
    final int expPos = Math.max(str.indexOf('e'), str.indexOf('E'));
    int numDecimals = 0; // Check required precision
    if (decPos > -1) { // there is a decimal point
        if (expPos > -1) { // there is an exponent
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
        numDecimals = dec.length();
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        exp = expPos > -1 ? str.substring(expPos + 1) : null;
        final String numeric = str.substring(0, str.length() - 1);
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null && isDigits(numeric)) {
                    return createLong(numeric);
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f':
            case 'F':
            case 'd':
            case 'D':
                if (numDecimals > 15 || expPos > -1) {
                    return createBigDecimal(numeric);
                } else if (numDecimals > 7) {
                    return createDouble(numeric);
                } else {
                    return createFloat(numeric);
                }
            default:
                throw new NumberFormatException(str + " is not a valid number.");
        }
    }
    // No type suffix, infer type based on content
    if (dec == null && exp == null) {
        try {
            return createInteger(str);
        } catch (NumberFormatException e) {
            try {
                return createLong(str);
            } catch (NumberFormatException e1) {
                return createBigInteger(str);
            }
        }
    }
    // Floating point number
    final boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
    if (dec != null) {
        if (numDecimals > 15 || expPos > -1) {
            return createBigDecimal(str);
        } else if (numDecimals > 7) {
            return createDouble(str);
        } else {
            try {
                Float f = createFloat(str);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException ignored) {
            }
            return createBigDecimal(str);
        }
    }
    return createBigDecimal(str);
}