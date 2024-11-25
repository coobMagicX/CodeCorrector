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

    // Deal with any possible sign up front
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
        } else if ((sz == start + 2) && (chars[start] == '0') && (chars[start + 1] >= 'l' && chars[start + 1] <= 'L')) {
            // Handle the case for "0L"
            return true;
        }
    }

    sz--; 
    int i = start;

    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false; 
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            // We've already taken care of hex.
            if (hasExp) {
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
            foundDigit = false; // We need a digit after the E
        } else {
            if ((i + 1 < sz && 
                    (chars[i] == 'l' || chars[i] == 'L') && 
                    (chars[i+1] >= 'd' && chars[i+1] <= 'D')) ||
                (i + 1 < sz && 
                    (chars[i] == 'l' || chars[i] == 'L') && 
                    (chars[i+1] >= 'f' && chars[i+1] <= 'F'))) {
                // Handle the case for "L" or "l"
                return foundDigit;
            } else if ((i + 1 < sz && 
                        (chars[i] == 'd' || chars[i] == 'D') &&
                        (chars[i+1] >= 'l' && chars[i+1] <= 'L')) ||
                       (i + 1 < sz && 
                        (chars[i] == 'f' || chars[i] == 'F') &&
                        (chars[i+1] >= 'l' && chars[i+1] <= 'L'))) {
                // Handle the case for "L" or "l"
                return foundDigit;
            } else if ((i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'd' && chars[i+1] <= 'D')) ||
                       (i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'f' && chars[i+1] <= 'F'))) {
                // Handle the case for "L" or "l"
                return foundDigit;
            } else if ((i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'D' && chars[i+1] <= 'd')) ||
                       (i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'F' && chars[i+1] <= 'f'))) {
                // Handle the case for "L" or "l"
                return foundDigit;
            } else if ((i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'D' && chars[i+1] <= 'd')) ||
                       (i + 1 < sz && 
                        (chars[i] == 'e' || chars[i] == 'E') &&
                        (chars[i+1] >= 'F' && chars[i+1] <= 'f'))) {
                // Handle the case for "L" or "l"
                return foundDigit;
            } else {
                return false; 
            }
        }

        i++;
    }

    if ((foundDigit) || hasDecPoint) {
        return true;
    } else {
        return false;
    }
}