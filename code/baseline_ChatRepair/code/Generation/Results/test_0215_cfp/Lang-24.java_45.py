public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExponent = false;
    boolean hasDecimalPoint = false;
    boolean hasSign = false;
    boolean foundDigit = false;

    int start = (chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        return false; // to skip checking hexadecimal numbers, add proper hex handling if necessary
    }

    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            hasSign = false; // reset because we've now seen a digit after the sign
        } else if (chars[i] == '.') {
            if (hasDecimalPoint || hasExponent) return false;
            hasDecimalPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExponent || !foundDigit) return false;
            hasExponent = true;
            hasSign = false; // expect at least one digit after 'e' or 'E'
            foundDigit = false; // reset to ensure digits follow exponent
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!hasExponent || hasSign) return false;
            hasSign = true;
            foundDigit = false; // reset to ensure digits follow the sign
        } else {
            return false; // current character is not allowed
        }
    }

    return foundDigit;
}
