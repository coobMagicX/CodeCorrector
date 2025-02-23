public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExponent = false;
    boolean hasDecimalPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;

    int start = (chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
        // hex numbers
        for (int i = start + 2; i < length; i++) {
            if((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    // remove type qualifiers from the end
    int end = length;
    while (end > start && (chars[end - 1] == 'd' || chars[end - 1] == 'D' || chars[end - 1] == 'f' || chars[end - 1] == 'F')) {
        end--;
    }

    // continue with number validation
    int i = start;
    while (i < end) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecimalPoint || hasExponent) {
                return false;
            }
            hasDecimalPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExponent || !foundDigit) {
                return false;
            }
            hasExponent = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false;
        } else {
            return false;
        }
        i++;
    }
    return foundDigit && !allowSigns;
}
