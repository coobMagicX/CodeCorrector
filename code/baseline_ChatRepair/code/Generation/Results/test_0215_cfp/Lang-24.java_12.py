public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }

    char[] chars = str.toCharArray();
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;
    boolean lastCharIsLegal = false;

    int len = chars.length;
    int start = chars[0] == '-' ? 1 : 0;

    if (len > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
        int i = start + 2;
        if (i == len) return false; // "0x"
        for (; i < len; i++) {
            if ((chars[i] < '0' || chars[i] > '9') &&
                (chars[i] < 'a' || chars[i] > 'f') &&
                (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    for (int i = start; i < len; i++) {
        char c = chars[i];
        if (c >= '0' && c <= '9') {
            foundDigit = true;
            lastCharIsLegal = true;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
            lastCharIsLegal = false;
        } else if (c == 'e' || c == 'E') {
            if (!foundDigit || hasExp) return false;
            hasExp = true;
            foundDigit = false; // Reset after 'e'
            lastCharIsLegal = false;
        } else if (c == '+' || c == '-') {
            if (!lastCharIsLegal) return false;
            lastCharIsLegal = false;
        } else {
            return false;
        }
    }

    return foundDigit;
}
