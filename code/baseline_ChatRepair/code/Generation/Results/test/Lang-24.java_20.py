public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    // handle possible sign at the beginning
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (chars.length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        return false; // Hexadecimal numbers are not supported.
    }

    for (int i = start; i < chars.length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
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
            foundDigit = false; // Reset for the digits after 'e' or 'E'
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!(chars[i - 1] == 'e' || chars[i - 1] == 'E')) {
                return false;
            }
        } else {
            // any other character makes this invalid
            return false;
        }
    }
    return foundDigit;
}
