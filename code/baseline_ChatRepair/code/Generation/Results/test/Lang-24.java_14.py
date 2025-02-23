public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    str = str.trim();  // Trim leading and trailing whitespace

    // Attempt to catch hex numbers early
    if (str.startsWith("-0x") || str.startsWith("0x") || str.startsWith("+0x")) {
        try {
            Long.parseLong(str.substring(2), 16); // Parse as hexadecimal
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    if (str.matches("[-+]?\\d*\\.\\d+")) {  // Match decimals
        return true;
    }

    if (str.matches("[-+]?\\d+")) {  // Match integers
        return true;
    }

    // Matching scientific notation
    if (str.matches("[-+]?\\d+(\\.\\d+)?[eE][-+]?\\d+")) {
        return true;
    }

    // Additional floating types that may end in f, d, or F, D (float or double in Java)
    if (str.matches("[-+]?\\d*\\.\\d+[fFdD]")) {  // Match floating-point types with suffix f or d
        return true;
    }

    if (str.matches("[-+]?\\d+[fFdD]")) {  // Match decimal numbers integers ending in f or d
        return true;
    }

    return false;
}
