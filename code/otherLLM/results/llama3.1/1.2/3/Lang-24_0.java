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
            // checking hex (it can't be anything else)
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
          // for type qualifiers
    int i = start;
    // loop to the next to last char or to the last char if we need another digit to
    // make a valid number (e.g. chars[0..5] = "1234E")
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                // two decimal points or dec in exponent   
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            // we've already taken care of hex.
            if (hasExp) {
                // two E's
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
            foundDigit = false; // we need a digit after the E
        } else if (chars[i] == 'l' || chars[i] == 'L') {
            if (hasExp || hasDecPoint) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
        } else {
            return false;
        }
        i++;
    }
    // allowSigns is true iff the val ends in 'E'
    // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
    return !allowSigns && foundDigit;
}