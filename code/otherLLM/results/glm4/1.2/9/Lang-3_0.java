public static Number createNumber(String str) {
    if (str == null || str.isEmpty()) return null;

    // Attempt parsing with BigDecimal first as it can handle most numeric formats.
    try {
        return new BigDecimal(str);
    } catch (NumberFormatException e) {
        // Fallback to other types...
    }

    // Check for integer or long suffix
    if (str.endsWith("l") || str.endsWith("L")) {
        str = str.substring(0, str.length() - 1);
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            // Fallback to other types...
        }
    }

    // Check for float or double suffix
    if (str.endsWith("f") || str.endsWith("F")) {
        str = str.substring(0, str.length() - 1);
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            // Fallback to other types...
        }
    }

    // Check for double suffix
    if (str.endsWith("d") || str.endsWith("D")) {
        str = str.substring(0, str.length() - 1);
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            // Fallback to other types...
        }
    }

    // If all else fails, attempt parsing as an integer
    try {
        return Integer.parseInt(str);
    } catch (NumberFormatException e) {
        // Could log the error or handle it appropriately here
        return null;
    }
}