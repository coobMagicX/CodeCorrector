public static Number createNumber(String str) throws NumberFormatException {
    if (str == null || str.isEmpty()) {
        return null;
    }

    // Handle scientific notation with single 'e' or 'E'
    if (str.matches(".*[eE].*") && !str.contains("e") && !str.contains("E")) {
        return Double.parseDouble(str + "d"); // Append 'd' to convert to double
    }

    // Handle other numeric formats
    try {
        if (str.endsWith("l") || str.endsWith("L")) {
            return Long.parseLong(str.substring(0, str.length() - 1));
        } else if (str.endsWith("f") || str.endsWith("F")) {
            return Float.parseFloat(str.substring(0, str.length() - 1));
        } else if (str.endsWith("d") || str.endsWith("D")) {
            return Double.parseDouble(str);
        }
    } catch (NumberFormatException e) {
        // Ignore the bad number and proceed to other types
    }

    // Fallback to BigInteger for string that don't end with a numeric suffix
    try {
        return new java.math.BigInteger(str);
    } catch (NumberFormatException e) {
        throw new NumberFormatException("Cannot convert '" + str + "' to a valid number.");
    }
}

private static boolean isAllZeros(String str) {
    if (str == null) {
        return true;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
        if (str.charAt(i) != '0') {
            return false;
        }
    }
    return str.length() > 0;
}