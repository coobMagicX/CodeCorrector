public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    boolean hasDecimalPoint = false;
    boolean hasExponent = false;
    boolean allowSigns = false;
    boolean foundDigit = false;

    int start = 0;
    if (chars[start] == '-' || chars[start] == '+') {
        start++;
    }

    for (int i = start; i < chars.length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasExponent || hasDecimalPoint) {
                return false;
            }
            hasDecimalPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExponent || !foundDigit) {
                return false;
            }
            hasExponent = true;
            allowSigns = true;
            foundDigit = false; // Reset for new numeric sequence after exponent
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // Reset since sign should be followed by digits
        } else {
            return false;
        }
    }

    return foundDigit;
}
