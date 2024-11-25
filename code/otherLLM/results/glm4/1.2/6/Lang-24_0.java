public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int len = chars.length;

    // Handle negative numbers or decimal point at the beginning.
    int start = (chars[0] == '-') ? 1 : 0;

    // Check for hexadecimal prefix "0x" or "0X".
    if (start < len && chars[start] == '0') {
        if (start + 1 < len && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
            start += 2;
            if (start >= len) {
                return false; // "0x" or "0X" at the end of the string.
            }
        }
    }

    boolean hasDecimal = false;
    boolean hasExponent = false;
    int i = start;

    while (i < len) {
        char c = chars[i];

        if (c >= '0' && c <= '9') {
            // Digit
        } else if (c == '.') {
            if (hasDecimal || hasExponent) {
                return false; // Second or subsequent decimal point.
            }
            hasDecimal = true;
        } else if ((c == 'e' || c == 'E') && !hasExponent) {
            if (i + 1 < len && (chars[i + 1] == '+' || chars[i + 1] == '-')) {
                i++; // Skip the sign after exponent.
            }
            hasExponent = true;
        } else if ((c == 'd' || c == 'D' || c == 'f' || c == 'F') && !hasDecimal && !hasExponent) {
            return false; // Type specifiers are not allowed at the start.
        } else if (c == 'l' || c == 'L') {
            if (i + 1 < len && chars[i + 1] != 'u' && chars[i + 1] != 'U') {
                return false; // 'l' or 'L' must be followed by 'u' or 'U' to be valid.
            }
        } else {
            return false; // Invalid character for a number.
        }

        i++;
    }

    return true;
}