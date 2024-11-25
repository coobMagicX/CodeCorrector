public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;
    
    // deal with any possible sign up front
    int start = (chars[0] == '-') ? 1 : 0;
    if (sz > start + 1) {
        if (chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str == "0x"
            }
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9')
                    && (chars[i] < 'a' || chars[i] > 'f')
                    && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
    }
    sz--; // don't want to loop to the last char, check it afterwords
    
    int i = start;
    while (i < sz) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false; // two decimal points or dec in exponent
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                return false; // two E's
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
            foundDigit = false; // we need a digit after the E
        } else {
            return false;
        }
        i++;
    }

    // Check for invalid characters at the end of the string.
    if (i < chars.length) {
        char lastChar = chars[i];
        switch (lastChar) {
            case 'l':
            case 'L':
                if (!hasExp && !hasDecPoint) return foundDigit; // Allow 'L' or 'l' if there's no exponent or decimal point
                break;
            case 'd':
            case 'D':
            case 'f':
            case 'F':
                // If the last character is a type qualifier and we have a digit, it's a valid number.
                return foundDigit;
        }
    }

    return !allowSigns && foundDigit;
}