public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }

    char[] chars = str.toCharArray();
    int size = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean isPreviousExp = false;
    boolean foundDigit = false;
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;

    // Handling hexadecimal numbers
    if (size > start + 1 && chars[start] == '0' && (chars[start+1] == 'x' || chars[start+1] == 'X')) {
        for (int i = start + 2; i < chars.length; i++) {
            if ((chars[i] < '0' || chars[i] > '9')
                && (chars[i] < 'a' || chars[i] > 'f')
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    for (int i = start; i < chars.length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            isPreviousExp = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit || isPreviousExp) {
                return false;
            }
            hasExp = true;
            foundDigit = false;
            isPreviousExp = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!isPreviousExp) {
                return false;
            }
            foundDigit = false;
            isPreviousExp = false;
        } else {
            return false;
        }
    }
    return foundDigit;
}
