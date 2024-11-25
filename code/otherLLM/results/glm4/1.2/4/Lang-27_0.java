public static Number createNumber(String str) {
    if (str == null || StringUtils.isBlank(str)) {
        return null; // or throw new NumberFormatException("A blank string is not a valid number");
    }
    
    // Handle hexadecimal numbers and integers with leading minus.
    if (str.startsWith("-0x") || str.startsWith("0x")) {
        return createInteger(str);
    }

    try {
        // Check if it's a floating-point or double-precision number.
        Number parsed = Double.parseDouble(str);
        return parsed;
    } catch (NumberFormatException e) {
        // Handle other numeric types and formats if needed
        // For this example, we'll just return null for now.
        return null;
    }
}