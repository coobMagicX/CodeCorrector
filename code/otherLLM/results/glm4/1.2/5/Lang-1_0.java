// Check if the string contains only digits
boolean isNumericString = isDigits(str);

// Try to create an Integer
if (isNumericString) {
    try {
        return createInteger(str);
    } catch (NumberFormatException e) {
        // Handle the exception, possibly by trying another number type
    }
}

// If the previous attempt failed, try to create a Long
try {
    return createLong(str);
} catch (NumberFormatException e) {
    // Handle the exception, possibly by trying another number type
}

// If the string contains a decimal point, it might be a Float or Double
if (str.contains(".")) {
    // Determine the number of digits after the decimal point
    int decimalPointIndex = str.indexOf('.');
    int numberOfDigitsAfterDecimal = str.length() - decimalPointIndex - 1;
    
    // Try to create a Float if there are 7 or fewer digits after the decimal point
    if (numberOfDigitsAfterDecimal <= 7) {
        try {
            return createFloat(str);
        } catch (NumberFormatException e) {
            // Handle the exception, possibly by trying another number type
        }
    }

    // Try to create a Double if there are between 8 and 16 digits after the decimal point
    if (numberOfDigitsAfterDecimal <= 16) {
        try {
            return createDouble(str);
        } catch (NumberFormatException e) {
            // Handle the exception, possibly by trying another number type
        }
    }
}

// If none of the above types work, attempt to parse a BigDecimal
try {
    return createBigDecimal(str);
} catch (NumberFormatException e) {
    // Handle the exception, indicating that the string is not a valid number
}