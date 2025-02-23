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

    int start = (chars[0] == '-') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // check hex (0x or 0X)
        for (int i = start + 2; i < sz; i++) {
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    sz--; // decrement sz to loop till the last character before any possible end letters

    for (int i = start; i < sz; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false;
        } else {
            return false; // invalid character found
        }
    }

    if (chars[sz] >= '0' && chars[sz] <= '9') {
        return true;
    }
    if (chars[sz] == 'e' || chars[sz] == 'E') {
        return false;
    }
    if (chars[sz] == '.') {
        return foundDigit && !hasExp; // decimal point at end is valid if no exponent part
    }
    if (!allowSigns && (chars[sz] == 'd' || chars[sz] == 'D' || chars[sz] == 'f' || chars[sz] == 'F')) {
        return foundDigit; // encountering d,D,f,F at end is fine if a digit has been found
    }
    if (chars[sz] == 'l' || chars[sz] == 'L') {
        return foundDigit && !hasExp && !hasDecPoint; // L at end is valid only in integers
    }
    return false;
}
