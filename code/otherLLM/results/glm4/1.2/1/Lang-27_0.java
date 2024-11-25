public Number create(String str) {
    // Check for '0x'/'-0x' prefix and return integer/BigInteger accordingly.
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        return createInteger(str);
    }

    char lastChar = str.charAt(str.length() - 1);

    String mantissa;
    String exponent;

    int decimalPointIndex = str.indexOf('.');
    int expIndex = Math.max(str.indexOf('e'), str.indexOf('E')) + 1;

    if (decimalPointIndex != -1) {
        if (expIndex != -1 && expIndex < decimalPointIndex) {
            throw new NumberFormatException("Invalid format: exponent before decimal point.");
        }
        mantissa = str.substring(0, decimalPointIndex);
        exponent = expIndex > 0 ? str.substring(expIndex) : null;
    } else {
        mantissa = str;
        exponent = null;
    }

    if (!Character.isDigit(lastChar)) {
        switch (lastChar) {
            case 'l':
            case 'L':
                return createInteger(mantissa);
            case 'f':
            case 'F':
                return createFloat(mantissa);
            case 'd':
            case 'D':
                return createDouble(mantissa);
        }
    }

    // General type conversions
    if (exponent == null && !isAllZeros(mantissa)) {
        try {
            float f = Float.parseFloat(mantissa);
            if (!Float.isInfinite(f)) {
                return f;
            }
        } catch (NumberFormatException e) {
            // Ignore
        }

        try {
            double d = Double.parseDouble(mantissa);
            if (!Double.isInfinite(d)) {
                return d;
            }
        } catch (NumberFormatException e) {
            // Ignore
        }
    }

    return createBigInteger(mantissa);
}