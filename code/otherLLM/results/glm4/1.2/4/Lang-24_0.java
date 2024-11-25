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
            for (; i < chars.length; i++) {
                char ch = chars[i];
                if ((ch < '0' || ch > '9')
                    && (ch < 'a' || ch > 'f')
                    && (ch < 'A' || ch > 'F')) {
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

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false; // two decimal points or dec in exponent
            }
            hasDecPoint = true;
            allowSigns = true; // next char must be a sign

        } else if ((chars[i] == 'e' || chars[i] == 'E') && !hasExp) {
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;

        } else if (chars[i] == '+' || chars[i] == '-' && allow Signs) {
            allowSigns = false;

        } else {
            return false; // invalid char
        }

        i++;
    }

    // Check for trailing characters
    if (i < sz) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            // no type qualifier, OK
            return true;
        }
        if (chars[i] == 'e' || chars[i] == 'E') {
            // can't have an E at the last byte
            return false;
        }
        if (chars[i] == '.') {
            // single trailing decimal point after non-exponent is ok
            return foundDigit;
        }
        if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
            return foundDigit;
        }
        if (chars[i] == 'l' || chars[i] == 'L') {
            // not allowing L with an exponent or decimal point
            return foundDigit && !hasExp;
        }
        // last character is illegal
        return false;
    }

    return !allowSigns && foundDigit; // allowSigns is true iff the val ends in 'E'
}