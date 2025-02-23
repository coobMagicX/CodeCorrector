public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    
    // Handle the sign at the beginning of the string
    int start = (chars[0] == '+' || chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0') {
        if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
            // Handle hexadecimal
            return isHexNumber(chars, start + 2);
        }
    }

    for (int i = start; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit) {
                return false;
            }
            hasExp = true;
            // Reset for numbers after exponent
            foundDigit = false;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!foundDigit || (i > 0 && (chars[i - 1] != 'e' && chars[i - 1] != 'E'))) {
                return false;
            }
        } else {
            return false;
        }
    }

    return foundDigit;
}

private static boolean isHexNumber(char[] chars, int start) {
    if (start >= chars.length) {
        return false;
    }
    for (int i = start; i < chars.length; i++) {
        if ((chars[i] < '0' || chars[i] > '9')
            && (chars[i] < 'a' || chars[i] > 'f')
            && (chars[i] < 'A' || chars[i] > 'F')) {
            return false;
        }
    }
    return true;
}
