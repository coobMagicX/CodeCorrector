public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    // deal with any possible sign up front
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // handling hexadecimal
        for (int i = start + 2; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                // two decimal points or decimal points after exponent
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit) {
                // two E's or 'E' without leading digits
                return false;
            }
            hasExp = true;
            // reset for numbers after exponent
            foundDigit = false;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!foundDigit || (i > 0 && (chars[i-1] != 'e' && chars[i-1] != 'E'))) {
                // sign not allowed or not immediately after an exponent
                return false;
            }
        } else {
            return false;
        }
    }
    return foundDigit;
}
