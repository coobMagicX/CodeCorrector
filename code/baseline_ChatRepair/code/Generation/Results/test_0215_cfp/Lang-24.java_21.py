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
    
    // Handle any possible sign in the beginning
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
        int i = start + 2;
        if (i == sz) return false; // "0x" is not valid
        
        for (; i < sz; i++) {
            // Check hexadecimal validity
            if ((chars[i] < '0' || chars[i] > '9') && 
                (chars[i] < 'a' || chars[i] > 'f') && 
                (chars[i] < 'A' || chars[i] > 'F')) return false;
        }
        return true;
    }
    sz--; // Reduce size to avoid confusion when checking the last character

    int i = start;
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            // Has to have found a digit before 'e' or 'E'
            if (hasExp || !foundDigit) return false;
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) return false;
            allowSigns = false;
            foundDigit = false; // A digit is required after the sign
        } else {
            return false;
        }
        i++;
    }
    if (i < sz) {
        if (chars[i] >= '0' && chars[i] <= '9' || (allowSigns && (chars[i] == '+' || chars[i] == '-'))) return true;
    } else {
        if (chars[i] == '.') {
            if (hasDecPoint || hasExp) return false;
            // An isolated decimal point not allowed without previous digits
            return foundDigit;
        }
        // Check the last character for valid qualifiers
        return !allowSigns && foundDigit;
    }
    return !allowSigns && foundDigit;
}
