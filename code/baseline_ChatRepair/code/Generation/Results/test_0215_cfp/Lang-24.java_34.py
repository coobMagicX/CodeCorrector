public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    boolean allowSigns = false;
    
    // Deal with possible leading sign
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (chars.length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        return isHexadecimal(chars, start + 2);
    }

    for (int i = start; i < chars.length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // reset foundDigit for valid number after 'E'
        } else {
            return false;
        }
    }
    return foundDigit;
}

// Helper function to handle hexadecimal numbers
private static boolean isHexadecimal(char[] chars, int start) {
    if (start == chars.length) return false; // String is "0x"
    for (int i = start; i < chars.length; i++) {
        if (!((chars[i] >= '0' && chars[i] <= '9') || 
              (chars[i] >= 'a' && chars[i] <= 'f') ||
              (chars[i] >= 'A' && chars[i] <= 'F'))) {
            return false;
        }
    }
    return true; // Valid hex
}
