public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int length = chars.length;
    int index = (chars[0] == '-') ? 1 : 0;
    
    if (length > index + 1 && chars[index] == '0' && (chars[index+1] == 'x' || chars[index+1] == 'X')) {
        int i = index + 2;
        if (i >= length) return false;
        // Checking hexadecimal (handle both cases where 'x' or 'X' could be present)
        for (; i < length; i++) {
            if ((chars[i] < '0' || chars[i] > '9') 
                && (chars[i] < 'a' || chars[i] > 'f') 
                && (chars[i] < 'A' || chars[i] > 'F')) return false;
        }
        return true;
    }

    boolean hasDecPoint = false;
    boolean hasExp = false;
    boolean foundDigit = false;
    boolean allowSigns = false;

    for (int i = index; i < length; i++) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp || !foundDigit) return false;
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) return false;
            allowSigns = false;
            foundDigit = false; // reset after exponent sign
        } else if (allowSigns || (!Character.isDigit(chars[i]) 
                                  && chars[i] != 'd' && chars[i] != 'D'
                                  && chars[i] != 'f' && chars[i] != 'F')) {
            return false;
        }
    }

    return foundDigit;
}
