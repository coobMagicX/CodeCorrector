public static Number createNumber(String val) throws NumberFormatException {
    if (val == null || val.isEmpty()) {
        throw new NumberFormatException("null or empty string is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null; // this is protection for poorness in java.lang.BigDecimal.
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }   
    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = val.indexOf('.');
    int expPos = val.indexOf('e') + val.indexOf('E');
    if (expPos == -1) {
        expPos = val.length();
    }

    // Split the string into mantissa, decimal part and exponent
    if (decPos > -1) {
        mant = val.substring(0, decPos);
        dec = val.substring(decPos + 1, expPos);
    } else {
        mant = val;
        dec = null;
    }
    if (expPos < val.length()) {
        exp = val.substring(expPos + 1);
    } else {
        exp = null;
    }

    // Check for non-numeric characters at the end of the string
    if (!Character.isDigit(lastChar) && !isSuffixValid(lastChar)) {
        throw new NumberFormatException(val + " is not a valid number.");
    }
    
    // Handle different numeric types based on suffix
    switch (lastChar) {
        case 'l':
        case 'L':
            try {
                return createLong(mant);
            } catch (NumberFormatException nfe) {
                return createBigInteger(mant);
            }
        case 'f':
        case 'F':
            if (dec == null && exp == null) {
                try {
                    float f = Float.parseFloat(mant);
                    if (!Float.isInfinite(f) && !isAllZeros(mant)) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
            }
        case 'd':
        case 'D':
            if (dec == null && exp == null) {
                try {
                    double d = Double.parseDouble(mant);
                    if (!Double.isInfinite(d) && !isAllZeros(mant)) {
                        return d;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
            }
        default:
            break; // Continue with default processing
    }

    // If we reached here, the string is not a valid numeric value
    throw new NumberFormatException(val + " is not a valid number.");
}

private static boolean isSuffixValid(char suffix) {
    return suffix == 'l' || suffix == 'L' || suffix == 'f' || suffix == 'F' || suffix == 'd' || suffix == 'D';
}