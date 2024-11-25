static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) {
        return false;
    }

    // Check for hexadecimal prefixes: "0x" or "0X"
    if ((s.charAt(0) == '0' && (s.length() > 1 && (s.charAt(1) == 'x' || s.charAt(1) == 'X'))) {
        return isHexadecimal(s.substring(2));
    }

    // Check for integer part
    int startIndex = s.charAt(0) != '-' ? 0 : 1; // Skip negative sign if present
    boolean hasDecimalPoint = false;
    for (int index = startIndex; index < len; index++) {
        char c = s.charAt(index);
        if (c == '.') { // Handle decimal points
            if (hasDecimalPoint) { // More than one decimal point is invalid
                return false;
            }
            hasDecimalPoint = true;
            startIndex = index + 1; // Start after the decimal point for checking integer part again
        } else if (c < '0' || c > '9') {
            return false; // Any non-numeric character other than a decimal point is invalid
        }
    }

    // If we reached here, it means all characters are numeric and there was at most one decimal point
    if (!hasDecimalPoint) { // It's an integer number
        long num;
        try {
            num = Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false; // Parsing to long should succeed for a simple integer
        }
        return true;
    }

    return false; // If we had a decimal point, it's not a simple number
}

static boolean isHexadecimal(String s) {
    if (s == null || s.length() == 0) {
        return false;
    }
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (!Character.isDigit(c, Character.MIN_RADIX)) {
            if (c != 'a' && c != 'b' && c != 'A' && c != 'B' && c != 'c' && c != 'd' && c != 'D' &&
                c != 'e' && c != 'E') {
                return false;
            }
        }
    }
    return true;
}