public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }

    // Trim any leading or trailing spaces.
    str = str.trim();

    // Check if the string is just an empty after trim.
    if (str.length() == 0) {
        return false;
    }

    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    // Check each character in the string.
    char[] chars = str.toCharArray();
    int start = (chars[0] == '+' || chars[0] == '-') ? 1 : 0; // skip the first sign

    // Edge case: only a sign is not a number.
    if (start == 1 && chars.length == 1) {
        return false;
    }

    for (int i = start; i < chars.length; i++) {
        char c = chars[i];
        if (Character.isDigit(c)) {
            foundDigit = true;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) {
                // Two decimal points or decimal in exponent.
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            if (hasExp || !foundDigit) {
                // Two exponents or 'e' without a number before it.
                return false;
            }
            hasExp = true;
            // Reset foundDigit as we need to find digits after the 'E'
            foundDigit = false;
            // The exponent can be followed by a sign.
            if (i + 1 < chars.length && (chars[i + 1] == '+' || chars[i + 1] == '-')) {
                i++; // skip the sign character
            }
        } else {
            return false; // Any other character other than digits, '.', 'e/E', signs is not acceptable.
        }
    }

    // Make sure characters after 'E' include digits (handled by foundDigit).
    return foundDigit;
}
