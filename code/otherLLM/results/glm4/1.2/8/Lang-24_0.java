public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasDecimalPoint = false;
    boolean hasExponent = false;

    for (int i = 0; i < sz; i++) {
        char c = chars[i];

        if (c >= '0' && c <= '9') { // Digits
            continue;
        } else if (c == '+' || c == '-') { // Sign
            if (i > 0) {
                return false; // Sign is not at the beginning or end of the string
            }
            continue;
        } else if (c == '.') { // Decimal point
            if (hasDecimalPoint || hasExponent) {
                return false; // More than one decimal point or a decimal point after an exponent
            }
            hasDecimalPoint = true;
            continue;
        } else if (c == 'e' || c == 'E') { // Exponent indicator
            if (hasExponent || i == 0 || chars[i - 1] == '+' || chars[i - 1] == '-') {
                return false; // More than one exponent or no digits before an exponent
            }
            hasExponent = true;
            continue;
        } else {
            return false; // Invalid character
        }
    }

    return true; // All characters are valid for a number
}