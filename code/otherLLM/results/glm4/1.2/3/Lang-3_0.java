public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null || StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank or null string is not a valid number");
    }
    
    // Handle hexadecimal numbers first
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
        } else if (hexDigits > 8) { // too many for an int
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
    }
    final boolean hasExponent = expPos > 0 && expPos < str.length() - 1;
    int numDecimals = 0;

    // Parse mantissa and decimal part
    if (decPos != -1) { // there is a decimal point
        if (!hasExponent) {
            throw new NumberFormatException("Missing exponent for floating-point number");
        }
        dec = str.substring(decPos + 1, expPos);
        numDecimals = dec.length();
        mant = str.substring(0, decPos);
    } else {
        mant = str;
    }

    // Parse exponent part
    if (hasExponent) {
        exp = str.substring(expPos + 1);
    } else {
        exp = null;
    }
    
    boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
    
    switch (lastChar) {
        case 'f':
        case 'F': // float
            try {
                final Float f = NumberUtils.createFloat(mant);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            break;
        case 'd':
        case 'D': // double
            try {
                final Double d = NumberUtils.createDouble(mant);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            break;
        default:
            // Default to BigDecimal if no explicit type is specified or other types are not suitable
            try {
                return createBigDecimal(mant + (exp != null ? "E" + exp : ""));
            } catch (NumberFormatException e) { // NOPMD
                throw new NumberFormatException("Invalid number format: " + str);
            }
    }
    
    throw new NumberFormatException("Invalid number type for string: " + str);
}