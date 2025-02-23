public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    str = str.trim(); 

    int length = str.length();
    if (length == 0) {
        return false;
    }

    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean hasSign = false;
    boolean foundDigit = false;

    // Check each character
    for (int i = 0; i < length; i++) {
        char c = str.charAt(i);
        if (Character.isDigit(c)) {
            foundDigit = true;
            hasSign = false;
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
            hasSign = false; // Reset for signs after 'E'
        } else if (c == '+' || c == '-') {
            if (hasSign || (!foundDigit && i > 0 && !(str.charAt(i - 1) == 'e' || str.charAt(i - 1) == 'E'))) {
                return false;
            }
            hasSign = true;
        } else {
            return false; // Unexpected character
        }
    }

    return foundDigit && !hasSign;
}
