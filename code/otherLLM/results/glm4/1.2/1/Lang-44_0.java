public static Number createNumber(String val) throws NumberFormatException {
    if (val == null || val.isEmpty()) {
        throw new NumberFormatException("Empty or null string is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null; // This value is explicitly handled as invalid.
    }
    char lastChar = Character.toLowerCase(val.charAt(val.length() - 1));
    
    // Handle hexadecimal numbers
    if ((val.startsWith("0x") || val.startsWith("-0x")) && val.length() > 2) {
        try {
            return createBigInteger(val.substring(2));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Hexadecimal number is not valid: " + val);
        }
    }

    // Split the string into mantissa and exponent parts
    String mantissa;
    String exponent = null;
    int decimalPointIndex = val.indexOf('.');
    int expIndex = Math.max(val.indexOf('e'), val.indexOf('E'));
    
    if (decimalPointIndex != -1) {
        mantissa = val.substring(0, decimalPointIndex);
        if (expIndex != -1 && expIndex > decimalPointIndex) {
            exponent = val.substring(decimalPointIndex + 1, expIndex).trim();
        } else if (expIndex != -1) {
            exponent = val.substring(expIndex + 1).trim();
        }
    } else {
        mantissa = val;
    }

    // Check for suffixes and convert to the appropriate type
    boolean allZeros = isAllZeros(mantissa);
    switch (lastChar) {
        case 'l':
        case 'L':
            if (exponent == null && !allZeros) {
                try {
                    return createLong(mantissa);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("The value is too large for a long: " + val);
                }
            } else {
                try {
                    return createBigInteger(val);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Invalid BigInteger format: " + val);
                }
            }
        case 'f':
        case 'F':
            if (exponent == null && !allZeros) {
                try {
                    Float f = createFloat(mantissa);
                    if (!f.isInfinite() && (f.floatValue() != 0.0F || allZeros)) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignored
                }
            }
        case 'd':
        case 'D':
            if (exponent == null && !allZeros) {
                try {
                    Double d = createDouble(mantissa);
                    if (!d.isInfinite() && (d.doubleValue() != 0.0D || allZeros)) {
                        return d;
                    }
                } catch (NumberFormatException e) {
                    // ignored
                }
            }
        default:
            // No suffix, check for integer formats
            if (exponent == null && !allZeros) {
                try {
                    Integer i = createInteger(mantissa);
                    return i;
                } catch (NumberFormatException e) {
                    // ignored
                }
            }
    }

    throw new NumberFormatException("Invalid number format: " + val);
}

private static boolean isAllZeros(String str) {
    for (char c : str.toCharArray()) {
        if (c != '0') return false;
    }
    return true;
}