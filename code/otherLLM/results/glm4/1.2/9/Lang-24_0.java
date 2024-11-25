public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;

    // Check for hexadecimal prefix
    if (chars.length > 2 && chars[0] == '0' && (chars[1] == 'x' || chars[1] == 'X')) {
        for (int i = 2; i < sz; i++) {
            char c = chars[i];
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
                return false;
            }
        }
        return true; // Entire string is a valid hexadecimal number
    }

    int start = chars[0] == '-' ? 1 : 0; // Start from 1 if negative, or 0 for positive numbers

    for (int i = start; i < sz; i++) {
        char c = chars[i];
        
        if (c >= '0' && c <= '9') {
            // Digit found
        } else if (c == '.') {
            if (hasDecPoint || hasExp) return false; // Too many decimal points or decimal in exponent
            hasDecPoint = true;
        } else if (c == '+' || c == '-') {
            if (!allowSigns) return false; // Unallowed sign
            allowSigns = false;
        } else if (c == 'e' || c == 'E') {
            if (hasExp) return false; // Two exponents in a row
            hasExp = true;
            allowSigns = true; // Allow signs after the exponent
        } else {
            // Invalid character for a number
            return false;
        }
    }

    return !allowSigns && foundDigit; // Valid if it ends with a digit and no unallowed characters
}