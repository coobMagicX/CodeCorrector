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
    // deal with any possible sign up front
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;

    // handling hex ("0x" or "0X")
    if (sz > start + 1 && chars[start] == '0' && 
        (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        int i = start + 2;
        if (i == sz) return false; // "0x" is not a valid number
        for (; i < sz; i++) {
            if ((chars[i] < '0' || chars[i] > '9')
                && (chars[i] < 'a' || chars[i] > 'f')
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    sz--; // check the last character separately
    int i = start;
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        char currentChar = chars[i];
        if (currentChar >= '0' && currentChar <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (currentChar == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
        } else if (currentChar == 'e' || currentChar == 'E') {
            if (hasExp) return false;
            if (!foundDigit) return false;
            hasExp = true;
            allowSigns = true;
        } else if (currentChar == '+' || currentChar == '-') {
            if (!allowSigns) return false;
            allowSigns = false;
            foundDigit = false; // reset because we need a digit after the sign
        } else {
            return false;
        }
        i++;
    }

    if (i < chars.length) {
        if (chars[i] >= '0' && chars[i] <= '9') return true;
        if (chars[i] == 'e' || chars[i] == 'E' || chars[i] == '+' || chars[i] == '-') return false;
        if (chars[i] == '.') {
            return foundDigit && !hasExp;
        }
    }

    return !allowSigns && foundDigit;
}
