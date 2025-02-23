public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // check hexadecimal
        for (int i = start + 2; i < length; i++) {
            char c = chars[i];
            if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                return false;
            }
        }
        return true; // hexadecimal is valid
    }

    boolean allowSigns = false;
    for (int i = start; i < length; i++) {
        char c = chars[i];
        
        // Check digits
        if (c >= '0' && c <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            if (hasExp || !foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (c == '+' || c == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false;  // reset because we need a digit after a sign following E
        } else {
            // any other character makes it invalid
            return false;
        }
    }

    // If the string ends in 'e' or 'E', it's not a valid number
    return foundDigit && !hasExp;
}
