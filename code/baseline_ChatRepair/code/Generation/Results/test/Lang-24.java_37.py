public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExponent = false;
    boolean hasDecimalPoint = false;
    boolean foundDigit = false;

    int start = (chars[0] == '+' || chars[0] == '-') ? 1 : 0;

    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        return false; // number is hexadecimal, not supported by specifications
    }

    boolean numberCanEnd = false;

    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            numberCanEnd = true;
        } else if (chars[i] == '.') {
            if (hasDecimalPoint || hasExponent) {
                return false;
            }
            hasDecimalPoint = true;
            numberCanEnd = false;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExponent || !foundDigit) {
                return false;
            }
            hasExponent = true;
            // Immediately allow signs as next char (plus check it in next iteration)
            numberCanEnd = false;
            foundDigit = false; // reset, as we need digits after the exponent
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!numberCanEnd) { // sign not allowed here
                return false;
            }
            numberCanEnd = false; // a number must follow signs
            foundDigit = false; // reset, as we need digits after the sign
        } else {
            if (numberCanEnd && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return true;  // allow these suffixes if conditions are met
            } else if (!hasExponent && !hasDecimalPoint && (chars[i] == 'l' || chars[i] == 'L')) {
                return foundDigit; // 'l' or 'L' should be last and no exponent/decimal
            } else {
                return false; // unknown character
            }
        }
    }
    return numberCanEnd;
}
