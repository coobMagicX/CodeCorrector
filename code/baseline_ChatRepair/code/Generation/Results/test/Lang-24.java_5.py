public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;

    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
        if (sz == start + 2) {
            return false; // String "0x" is not a number
        }
        for (int i = start + 2; i < sz; i++) {
            // Check if it is a valid hex number
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true; // Valid hexadecimal number
    }

    sz--; // Reduce the size to avoid checking the last index in the loop

    for (int i = start; i < sz || (i == sz && allowSigns); i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
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
            allowSigns = true;
            foundDigit = false; // Reset for the exponent part
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // Expect digit after sign
        } else {
            return false;
        }
    }

    // Final character checks:
    if (chars[sz] >= '0' && chars[sz] <= '9') {
        return true;
    } else if (chars[sz] == 'e' || chars[sz] == 'E' || chars[sz] == '+' || chars[sz] == '-') {
        return false;
    } else if (chars[sz] == '.') {
        return !hasExp && foundDigit;
    }

    return !allowSigns && foundDigit;
}
