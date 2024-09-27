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

    // Deal with any possible sign up front
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;

    if (sz > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        // Checking hexadecimal (it can't be anything else)
        int i = start + 2;
        if (i == sz) {
            return false; // String is "0x" or "0X"
        }
        for (; i < sz; i++) {
            if ((chars[i] < '0' || chars[i] > '9') 
                && (chars[i] < 'a' || chars[i] > 'f') 
                && (chars[i] < 'A' || chars[i] > 'F')) {
                return false;
            }
        }
        return true;
    }

    // Adjust to not loop to the last character, but check it afterwards for type qualifiers
    sz--;

    int i = start;
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false; // Two decimal points or decimal in exponent
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                return false; // Two exponents
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
            foundDigit = false; // Need a digit after the exponent
        } else {
            return false;
        }
        i++;
    }

    if (i < chars.length) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            return true;
        }
        if (chars[i] == 'e' || chars[i] == 'E') {
            return false;
        }
        if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            return foundDigit; // Single trailing decimal point after non-exponent is OK
        }
        if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
            return foundDigit;
        }
        if (chars[i] == 'l' || chars[i] == 'L') {
            return foundDigit && !hasExp && !hasDecPoint; // Not allowing 'L' with an exponent or decimal point
        }
        return false;
    }

    // AllowSigns is true iff the value ends in 'E'
    // Found digit is to make sure weird stuff like '.' and '1E-' doesn't pass
    return !allowSigns && foundDigit;
}