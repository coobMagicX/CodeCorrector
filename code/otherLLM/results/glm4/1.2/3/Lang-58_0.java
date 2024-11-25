public static Number create(String str) {
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E');
    
    // Remove trailing zeros and decimal point for comparison
    String numericWithoutDecimals = str.replaceAll("\\.0+$", "");
    String numericWithoutExponent = str.replaceAll("e[+-]?\\d+$", "");

    if (str.endsWith("l") || str.endsWith("L")) {
        return createLong(numericWithoutExponent);
    } else if (str.endsWith("f") || str.endsWith("F")) {
        return parseFloat(numericWithoutExponent);
    }

    // Check for other numeric types
    try {
        if (numericWithoutDecimals.matches("-?\\d+")) {
            return createInteger(str); // This would also handle BigInteger and Long
        }
        return parseFloat(numericWithoutExponent);
    } catch (NumberFormatException e) {
        throw new NumberFormatException("Invalid number format: " + str);
    }
}

private static Number createLong(String numeric) {
    try {
        return Long.parseLong(numeric);
    } catch (NumberFormatException e) {
        // Try BigInteger if it doesn't fit in a long
        return new BigInteger(numeric);
    }
}

private static Float parseFloat(String numeric) {
    try {
        return Float.parseFloat(numeric);
    } catch (NumberFormatException e) {
        // If it's too big for a float, use double as a fallback
        return Double.parseDouble(numeric).floatValue();
    }
}