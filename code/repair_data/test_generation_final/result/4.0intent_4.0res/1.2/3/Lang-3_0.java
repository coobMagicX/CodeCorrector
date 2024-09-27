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
            pfxLen = pfx.length();
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
    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point
        if (expPos > -1) { // there is an exponent
            if (expPos < decPos) { // prevents exponent before decimal causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
            exp = str.substring(expPos + 1);
        } else {
            dec = str.substring(decPos + 1);
            exp = null;
        }
        mant = str.substring(0, decPos);
        numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.
    } else {
        mant = (expPos > -1) ? str.substring(0, expPos) : str;
        dec = null;
        exp = (expPos > -1) ? str.substring(expPos + 1) : null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        final String numeric = str.substring(0, str.length() - 1);
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l':
            case 'L':
                return handleLongAndBigInteger(numeric, dec, exp);
            case 'f':
            case 'F':
                return handleFloat(numeric, allZeros);
            case 'd':
            case 'D':
                return handleDoubleAndBigDecimal(numeric, allZeros);
            default:
                throw new NumberFormatException(str + " is not a valid number.");
        }
    }
    // Default case for numbers without type suffix and handling floating point numbers and integers.
    return handleFloatingPointAndInteger(str, mant, dec, exp);
}

private static Number handleLongAndBigInteger(String numeric, String dec, String exp) {
    if (dec == null && exp == null && (numeric.charAt(0) == '-' ? isDigits(numeric.substring(1)) : isDigits(numeric))) {
        try {
            return createLong(numeric);
        } catch (NumberFormatException nfe) {
            return createBigInteger(numeric);
        }
    }
    throw new NumberFormatException(numeric + " is not a valid number.");
}

private static Number handleFloat(String numeric, boolean allZeros) {
    try {
        Float f = createFloat(numeric);
        if (!f.isInfinite() && !(f.floatValue() == 0.0F && !allZeros)) {
            return f;
        }
    } catch (NumberFormatException ignore) {
    }
    return handleDoubleAndBigDecimal(numeric, allZeros);
}

private static Number handleDoubleAndBigDecimal(String numeric, boolean allZeros) {
    try {
        Double d = createDouble(numeric);
        if (!d.isInfinite() && !(d.doubleValue() == 0.0D && !allZeros)) {
            return d;
        }
        return createBigDecimal(numeric);
    } catch (NumberFormatException ignore) {
        throw new NumberFormatException(numeric + " is not a valid number.");
    }
}

private static Number handleFloatingPointAndInteger(String str, String mant, String dec, String exp) {
    boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
    if (dec == null && exp == null) {
        try {
            return createInteger(str);
        } catch (NumberFormatException ignore) {
            try {
                return createLong(str);
            } catch (NumberFormatException ignoreAgain) {
                return createBigInteger(str);
            }
        }
    }
    try {
        Float f = createFloat(str);
        if (!f.isInfinite() && !(f.floatValue() == 0.0F && !allZeros)) {
            return f;
        }
    } catch (NumberFormatException ignore) {
    }
    try {
        Double d = createDouble(str);
        if (!d.isInfinite() && !(d.doubleValue() == 0.0D && !allZeros)) {
            return d;
        }
    } catch (NumberFormatException ignore) {
    }
    return createBigDecimal(str);
}