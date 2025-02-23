public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    // deal with any possible leading sign
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // hex format, check all characters are valid hex digits after "0x" or "0X"
        for (int i = start + 2; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9')
                && (chars[i] < 'a' || chars[i] > 'f')
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true; // valid hex
    }
    // limit the check to the next to last character
    int endIndex = length - 1;

    for (int i = start; i <= endIndex; i++) {
        char c = chars[i];
        if (c >= '0' && c <= '9') {
            foundDigit = true;
            continue;
        }

        if (c == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
            continue;
        }

        if (c == 'e' || c == 'E') {
            if (hasExp || !foundDigit) {
                return false;
            }
            hasExp = true;
            // Look for signs just after exponent and track it, need digit afterwards
            if (i < endIndex && (chars[i + 1] == '+' || chars[i + 1] == '-')) i++;
            foundDigit = false; // we need a digit after exponent and potential signs
            continue;
        }

        if (c == '+' || c == '-') {
            return false; // signs not at the start or just after 'e' or 'E' are not valid
        }

        return false; // any other characters are invalid
    }

    return foundDigit;
}
