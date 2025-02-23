public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExponent = false;
    boolean hasDecimalPoint = false;
    boolean foundDigit = false;

    // Deal with any possible sign up front
    int start = (chars[0] == '+' || chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // hex number
        int i = start + 2;
        if (i == length) return false; // string is "0x"
        for (; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9')
                && (chars[i] < 'a' || chars[i] > 'f')
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    // Normal numeric processing
    boolean allowSigns = false;
    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecimalPoint || hasExponent) {
                return false;
            }
            hasDecimalPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExponent) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExponent = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false;
        } else {
            return false;
        }
    }
    
    return foundDigit;
}
