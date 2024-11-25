public static Number createNumber(String str) throws NumberFormatException {
    if (str == null || StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank or null string is not a valid number");
    }

    // Normalize the string to lower case for consistent processing
    String normalizedStr = str.toLowerCase();

    if (normalizedStr.startsWith("0x") || normalizedStr.startsWith("-0x")) {
        return createInteger(normalizedStr);
    } else if (str.endsWith("d") || str.endsWith("D") || 
               str.endsWith("l") || str.endsWith("L")) {
        // Handle long values
        int startIndex = str.length() - 1;
        while (startIndex > 0 && Character.isDigit(str.charAt(startIndex))) {
            startIndex--;
        }
        String numericPart = str.substring(0, startIndex + 1);
        if (numericPart.startsWith("-")) {
            startIndex++;
        }
        if (isDigits(numericPart)) {
            return createLong(numericPart);
        } else {
            return createBigInteger(numericPart);
        }
    } else if (str.endsWith("f") || str.endsWith("F") || 
               str.endsWith("d") || str.endsWith("D")) {
        // Handle float and double values
        String numericPart = str.substring(0, str.length() - 1);
        if (isDigits(numericPart) || numericPart.contains(".")) {
            try {
                return NumberUtils.createDouble(numericPart);
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
        }
    }

    // If it's not a long, integer, float or double, then assume it's a BigDecimal
    return createBigDecimal(str);
}

// Placeholder methods to represent the actual implementation that would be used.
private static Integer createInteger(String numericPart) {
    // Implementation depends on whether the string is in decimal form.
    throw new NumberFormatException("Cannot parse as integer");
}

private static Long createLong(String numericPart) {
    // Implementation depends on whether the string is in decimal form or has a leading '-'.
    throw new NumberFormatException("Cannot parse as long");
}

private static BigInteger createBigInteger(String numericPart) {
    // Implementation for creating a BigInteger from a string.
    throw new NumberFormatException("Cannot parse as big integer");
}

private static Float createFloat(String numericPart) {
    // Implementation for creating a Float from a string.
    throw new NumberFormatException("Cannot parse as float");
}

private static Double createDouble(String numericPart) {
    // Implementation for creating a Double from a string.
    throw new NumberFormatException("Cannot parse as double");
}

private static BigDecimal createBigDecimal(String numericPart) {
    // Implementation for creating a BigDecimal from a string.
    return new BigDecimal(numericPart);
}

// Utility method to check if the string contains only digits.
private static boolean isDigits(String s) {
    for (int i = 0; i < s.length(); i++) {
        if (!Character.isDigit(s.charAt(i))) {
            return false;
        }
    }
    return true;
}