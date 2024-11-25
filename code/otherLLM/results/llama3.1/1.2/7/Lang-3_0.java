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
        return createInteger(str); // Added this line to return Integer when hex digits are between 9 and 15
    }

    final String numericPart = str.replaceAll("[^\\d\\.]", "");
    if (numericPart.isEmpty()) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    final String mantissa = numericPart.contains(".") ? numericPart : "";
    final int exponent = numericPart.contains(".") ? getExponent(numericPart) : 0;

    try {
        return createFloat(mantissa + "e" + exponent);
    } catch (final NumberFormatException e) { // NOPMD
        // ignore the bad number
    }
    try {
        final Double d = NumberUtils.createDouble(mantissa + "e" + exponent);
        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !isAllZeros(mantissa)))) {
            return d;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final Float f = createFloat(numericPart);
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !isAllZeros(mantissa)))) {
            return f;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final Double d = createDouble(numericPart);
        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !isAllZeros(mantissa)))) {
            return d;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final Float f = createFloat(numericPart);
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !isAllZeros(mantissa)))) {
            return f;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
        final Double d = createDouble(numericPart);
        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !isAllZeros(mantissa)))) {
            return d;
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return createBigDecimal(numericPart);
}

private static int getExponent(final String str) {
    final int decimalIndex = str.indexOf('.');
    if (decimalIndex == -1) {
        return 0;
    }
    return str.length() - decimalIndex - 1;
}