public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExponent = false;
    boolean hasDecimalPoint = false;
    boolean hasSignChars = false;
    boolean foundDigit = false;

    // handle potential leading '-' for negative numbers
    int start = (chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // handling hexadecimal numbers
        for (int i = start + 2; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true; // valid hexadecimal
    }

    int lastIndex = length - 1;
    for (int i = start; i < length; i++) {
        char currentChar = chars[i];
        if (currentChar >= '0' && currentChar <= '9') {
            foundDigit = true;
            hasSignChars = false;
        } else if (currentChar == '.') {
            if (hasDecimalPoint || hasExponent) {
                return false;
            }
            hasDecimalPoint = true;
        } else if (currentChar == 'e' || currentChar == 'E') {
            if (hasExponent || !foundDigit) {
                return false;
            }
            hasExponent = true;
            hasSignChars = true; // next character can be a sign char
        } else if (currentChar == '+' || currentChar == '-') {
            if (!hasSignChars || i == lastIndex) {
                return false;
            }
            hasSignChars = false; // prevent multiple signs
            foundDigit = false; // expect a digit next
        } else if (i == lastIndex && (currentChar == 'd' || currentChar == 'D' || currentChar == 'f' || currentChar == 'F')) {
            // floating point literals are fine if last character as long as there was a digit before them
            return foundDigit && !hasExponent;
        } else if (i == lastIndex && (currentChar == 'l' || currentChar == 'L')) {
            // integer literals are fine as last character if there was a digit and no decimal or exponent
            return foundDigit && !hasExponent && !hasDecimalPoint;
        } else {
            return false;
        }
    }

    return foundDigit;
}
