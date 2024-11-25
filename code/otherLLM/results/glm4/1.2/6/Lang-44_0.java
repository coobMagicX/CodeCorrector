public static Number createNumber(String val) {
    if (val == null || val.isEmpty()) {
        throw new IllegalArgumentException("Input string is null or empty");
    }

    int lastCharIndex = val.length() - 1;
    String numericPart = val.substring(0, lastCharIndex);

    boolean isIntegerType = val.endsWith("l") || val.endsWith("L") ||
                             (val.length() <= 1 || !Character.isDigit(val.charAt(lastCharIndex)));

    if (isIntegerType) {
        return createBigInteger(numericPart);
    } else {
        return createDecimalNumber(numericPart, val.charAt(lastCharIndex));
    }
}

private static Number createDecimalNumber(String numericPart, char lastChar) {
    switch (lastChar) {
        case 'f':
        case 'F':
            try {
                return Float.valueOf(numericPart);
            } catch (NumberFormatException e) {
                // Handle float conversion error
            }
        case 'd':
        case 'D':
            try {
                return Double.valueOf(numericPart);
            } catch (NumberFormatException e) {
                // Handle double conversion error
            }
        default:
            return createBigDecimal(numericPart);
    }
}