public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    // Handle the sign at the beginning of the string
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    // Hex numbers handling
    if (chars.length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        for (int i = start + 2; i < chars.length; i++) {
            char ch = chars[i];
            if ((ch < '0' || ch > '9') && (ch < 'a' || ch > 'f') && (ch < 'A' || ch > 'F')) {
                return false;
            }
        }
        return true;
    }

    boolean allowSigns = false; // set to true after an 'e' or 'E'

    for (int i = start; i < chars.length; i++) {
        char ch = chars[i];
        if (ch >= '0' && ch <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (ch == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (ch == 'e' || ch == 'E') {
            if (hasExp || !foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (ch == '+' || ch == '-') {
            if (!allowSigns) {
                return false;
            }
            // next character must be a digit or nothing
            if (i == chars.length - 1 || !(chars[i + 1] >= '0' && chars[i + 1] <= '9')) {
                return false;
            }
            allowSigns = false;
        } else {
            return false;
        }
    }
    return !hasExp || foundDigit; // correct check for the presence of digits in the case of scientific notation
}
