import org.apache.commons.lang3.math.NumberUtils;

public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }

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
    final int expPos = str.indexOf('e') + str.indexOf('E');
    if (expPos == -1) {
        expPos = str.length();
    }

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point
        mant = str.substring(0, decPos);
        dec = str.substring(decPos + 1, expPos);
        if (dec.length() > 0) {
            numDecimals = dec.length();
        }
    } else {
        mant = str;
        dec = null;
    }

    // Determine the type of number to return based on the suffix
    boolean isFloat = false, isDouble = false;
    if (lastChar == 'f' || lastChar == 'F') {
        isFloat = true;
    } else if (lastChar == 'd' || lastChar == 'D') {
        isDouble = true;
    }

    // Check the precision of the number
    double mantValue = 0.0;
    try {
        mantValue = Double.parseDouble(mant);
    } catch (NumberFormatException e) {
        throw new NumberFormatException(str + " is not a valid number.");
    }

    if (isFloat) {
        Float f = null;
        try {
            f = createFloat(str);
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        if (f != null && !f.isInfinite() && (!allZeros(mantValue, numDecimals))) {
            return f;
        }
    }

    if (isDouble) {
        Double d = null;
        try {
            d = createDouble(str);
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        if (d != null && !d.isInfinite() && (!allZeros(mantValue, numDecimals))) {
            return d;
        }
    }

    // If it's not a float or double, return BigDecimal
    return createBigDecimal(str);
}

private static boolean allZeros(double mantValue, int numDecimals) {
    // Check if the number has any non-zero digits after the decimal point
    if (numDecimals > 0) {
        for (int i = 0; i < numDecimals; i++) {
            if ((mantValue * Math.pow(10, i)) % 1 != 0.0) {
                return false;
            }
        }
    }
    // If there are no digits after the decimal point, check if it's zero
    return mantValue == 0.0;
}