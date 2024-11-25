public static Number createNumber(String str) throws NumberFormatException {
    if (str == null || str.isEmpty()) {
        return null;
    }
    // Check for repeated exponent characters
    int eIndex = str.indexOf('e');
    int EIndex = str.indexOf('E');
    if ((eIndex >= 0 && eIndex != str.lastIndexOf('e')) || (EIndex >= 0 && EIndex != str.lastIndexOf('E'))) {
        throw new NumberFormatException(str + " is not a valid number.");
    }
    
    // Parse mantissa and exponent
    int decPos = str.indexOf('.');
    int expPos = eIndex + 1;
    if (eIndex == -1) {
        expPos = str.length();
    }
    String mantissa = str.substring(0, Math.min(decPos >= 0 ? decPos : str.length(), expPos));
    String exponent = str.substring(expPos);

    // Check for valid characters after the decimal point or exponent
    if (decPos > -1 && !isDecimalValid(mantissa, decPos)) {
        throw new NumberFormatException(str + " is not a valid number.");
    }
    if (expPos < str.length() && (!exponent.isEmpty() || decPos == -1)) {
        if (!isExponentValid(exponent)) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
    }

    // Determine the type of return value
    if (str.endsWith("l") || str.endsWith("L")) {
        return createLong(mantissa);
    } else if (str.endsWith("f") || str.endsWith("F")) {
        return createFloat(mantissa);
    } else if (str.endsWith("d") || str.endsWith("D")) {
        return createDouble(mantissa);
    } else {
        // Default to BigDecimal for non-type-specific input
        return createBigDecimal(mantissa + (exponent != null ? "E" + exponent : ""));
    }
}

private static boolean isDecimalValid(String mantissa, int decPos) {
    // Check if the part after the decimal point consists of only digits or zero
    for (int i = 0; i < mantissa.length(); i++) {
        char c = mantissa.charAt(i);
        if (!Character.isDigit(c) && c != '.') {
            return false;
        }
        if (c == '.' && i != decPos - 1) {
            // Decimal point must be at the end or start of the number
            return false;
        }
    }
    return true;
}

private static boolean isExponentValid(String exponent) {
    // Check if the exponent part consists of only digits or a single plus/minus sign
    if (exponent.startsWith("+") || exponent.startsWith("-")) {
        exponent = exponent.substring(1);
    }
    for (int i = 0; i < exponent.length(); i++) {
        char c = exponent.charAt(i);
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return true;
}