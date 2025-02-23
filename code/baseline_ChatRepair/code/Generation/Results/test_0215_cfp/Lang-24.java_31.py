public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;
    
    int start = (chars[0] == '-') ? 1 : 0;
    if (length > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        int i = start + 2;
        if (i == length) { // "0x" is not a number
            return false;
        }
        for (; i < chars.length; i++) {
            if ((chars[i] < '0' || chars[i] > '9')
                && (chars[i] < 'a' || chars[i] > 'f')
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true; // valid hexadecimal
    }
    
    length--; // Exclude the last char, it may be a type qualifier
    int i = start;
    
    while (i < length || (i < length + 1 && allowSigns && !foundDigit)) {
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
            foundDigit = false; // must find a digit after the exponent
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // reset because we need a digit after the sign
        } else {
            return false;
        }
        i++;
    }

    if (i < chars.length) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            return true;
        }
        if (chars[i] == 'e' || chars[i] == 'E' || chars[i] == 'D' || chars[i] == 'F') {
            return false;
        }
        if (chars[i] == '.') {
            return foundDigit && !hasExp;
        }
        return false;
    }
    return foundDigit && !allowSigns;
}
