public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean hasDigit = false;

    // Handling the sign at the beginning
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (length > start + 2 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        return false; // support for hex numbers can be handled separately
    }
    
    for (int i = start; i < length; i++) {
        char c = chars[i];
        if (Character.isDigit(c)) {
            hasDigit = true;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            if (hasExp || !hasDigit) {
                return false;
            }
            hasExp = true;
            hasDigit = false; // reset because exponent part must have digits
        } else if (c == '+' || c == '-') {
            // Sign can only come after an 'e' or 'E'
            if (!(chars[i - 1] == 'e' || chars[i - 1] == 'E')) {
                return false;
            }
        } else {
            // Invalid character
            return false;
        }
    }

    return hasDigit;
}
