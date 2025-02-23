public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    // deal with any possible sign up front
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
        int i = start + 2;
        if (i == sz) {
            return false; // returns false for "0x"
        }
        for (; i < chars.length; i++) {
            if ((chars[i] < '0' || chars[i] > '9') &&
                (chars[i] < 'a' || chars[i] > 'f') &&
                (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true; // valid hexadecimal
    }

    --sz; // Last character check for non-digit characters
    int i = start;
    while (i < sz || (i < sz + 1 && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;

        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit) { // 'e' or 'E' must follow a digit
                return false;
            }
            hasExp = true;
            // Next character can be a sign if not at end of string
            if (i < sz - 1 && (chars[i + 1] == '+' || chars[i + 1] == '-')) {
                i++; // Skip over the sign
            } else if (i >= sz - 1) {
                return false;
            }

        } else if (chars[i] == '+' || chars[i] == '-') {
            return false;
        } else {
            return false;
        }
        i++;
    }
    return foundDigit && chars[sz] != 'e' && chars[sz] != 'E';
}
