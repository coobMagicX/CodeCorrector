public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasDecimalPoint = false;
    boolean hasExponent = false;
    boolean foundDigit = false;

    // Iterate through the string to determine if it is a valid number
    for (int i = 0; i < sz; i++) {
        char c = chars[i];

        switch (c) {
            case '+':
            case '-':
                // A sign can only be at the beginning or after an exponent without a decimal point
                if (i == 0 || (hasExponent && !hasDecimalPoint)) {
                    hasExponent = true;
                    continue;
                }
                return false;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                foundDigit = true;
                if (hasExponent && !hasDecimalPoint) {
                    hasExponent = false; // Reset exponent flag after a digit
                }
                continue;

            case '.':
                if (!hasDecimalPoint && !hasExponent) {
                    hasDecimalPoint = true;
                } else {
                    return false; // More than one decimal point or decimal without digits
                }
                break;

            case 'e':
            case 'E':
                if (foundDigit && !hasExponent) { // An exponent can only come after a digit and before another digit
                    hasExponent = true;
                    continue;
                }
                return false; // Exponent with no preceding digits

            default:
                // This is not part of the valid number characters, check for allowed types specifiers
                if (c == 'd' || c == 'D' || c == 'f' || c == 'F') {
                    if (!hasDecimalPoint && !hasExponent) {
                        continue;
                    }
                } else if (c == 'l' || c == 'L') {
                    if (i != sz - 1 || hasExponent || hasDecimalPoint) {
                        return false; // 'l' or 'L' not allowed here
                    }
                    continue;
                }
                return false; // Invalid character
        }

        // If we reach the end of the string and have found at least one digit, it's a valid number
        if (i == sz - 1) {
            return !hasExponent && foundDigit;
        }
    }
    
    // If we exit the loop, the string is not a valid number
    return false;
}