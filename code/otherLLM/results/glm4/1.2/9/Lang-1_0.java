public Number createNumber(String str) {
    // Determine if there are type specifiers at the end of the string (e.g., 'l', 'f', 'd')
    int lastCharIndex = str.length() - 1;
    char lastChar = str.charAt(lastCharIndex);
    boolean isLong = lastChar == 'l' || lastChar == 'L';
    boolean isFloat = lastChar == 'f' || lastChar == 'F';
    boolean isDouble = lastChar == 'd' || lastChar == 'D';

    // Remove type specifiers for parsing
    if (isLong || isFloat || isDouble) {
        str = str.substring(0, lastCharIndex);
    }

    // Determine if there's a decimal point or exponent
    boolean hasDecimalPoint = str.indexOf('.') != -1;
    boolean hasExponent = str.indexOf('e') != -1 || str.indexOf('E') != -1;

    // Parse based on the presence of decimal points and exponents
    if (!hasDecimalPoint && !hasExponent) {
        // Integer or Long
        try {
            return isLong ? Long.parseLong(str) : Integer.parseInt(str);
        } catch (NumberFormatException e) {
            // Not an integer, proceed to next step
        }
    }

    // Determine which numeric type to use based on precision and magnitude
    if (hasDecimalPoint || hasExponent) {
        if (isFloat) {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException e) {
                // Not a float, proceed to next step
            }
        } else if (isDouble) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                // Not a double, proceed to next step
            }
        } else {
            // Use BigDecimal as the default type
            return new BigDecimal(str);
        }
    }

    throw new NumberFormatException("Unable to parse: " + str);
}