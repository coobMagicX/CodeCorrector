public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean wasSignAllowed = false;
    boolean foundDigit = false;

    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        int i = start + 2;
        if (i == sz) return false; // "0x" or "0X" is not a valid number
        for (; i < sz; i++) {
            if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    sz--;
    int i = start;
    while (i < sz || (i < sz + 1 && wasSignAllowed && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            wasSignAllowed = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) return false;
            if (!foundDigit) return false;
            hasExp = true;
            wasSignAllowed = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!wasSignAllowed) return false;
            wasSignAllowed = false;
            foundDigit = false; // must have a digit after the exponent
        } else {
            return false;
        }
        i++;
    }

    if (i < sz + 1) {
        return chars[i] >= '0' && chars[i] <= '9';
    } else {
        return !wasSignAllowed && foundDigit;
    }
}
