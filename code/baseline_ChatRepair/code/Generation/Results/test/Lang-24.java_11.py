public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    // Deal with possible sign at the beginning
    int start = (chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // Hexadecimal value check
        int i = start + 2;
        for (; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    // Check remaining characters
    boolean allowSigns = false;
    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // We need a digit after the E
        } else if ((chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || 
                    chars[i] == 'F' || chars[i] == 'l' || chars[i] == 'L') && foundDigit) {
            if (i + 1 < length) return false; // Type qualifiers should only appear at the end
        } else {
            return false;
        }
    }
    return foundDigit && !allowSigns;
}
