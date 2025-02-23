public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int length = chars.length;
    int index = 0;
    boolean foundDigit = false;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    
    // Handle the sign character at the start
    if (chars[index] == '+' || chars[index] == '-') {
        index++;
    }
    
    // Check hexadecimal numbers
    if (length > index + 2 && chars[index] == '0' && (chars[index + 1] == 'x' || chars[index + 1] == 'X')) {
        index += 2;
        if (index == length) return false; // string is "0x"
        
        for (; index < length; index++) {
            if ((chars[index] < '0' || chars[index] > '9') &&
                (chars[index] < 'a' || chars[index] > 'f') &&
                (chars[index] < 'A' || chars[index] > 'F')) {
                return false;
            }
        }
        return true;
    }

    // Normal number (decimal or scientific notation)
    boolean numeric = false;
    for (; index < length; index++) {
        char c = chars[index];
        if (c >= '0' && c <= '9') {
            foundDigit = true;
            numeric = true;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) {
                // Multiple decimal points or decimal in exponent
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            if (hasExp || !foundDigit) {
                // Multiple exponents or 'e' without leading digit
                return false;
            }
            hasExp = true;
            foundDigit = false; // expect at least a digit after 'e' or 'E'
        } else if (c == '+' || c == '-') {
            if (!numeric || chars[index - 1] != 'E' && chars[index - 1] != 'e') {
                return false;
            }
            foundDigit = false;
            numeric = false; // Sign after 'E', should not soloing affect numeric state
        } else {
            return false;
        }
    }
    return foundDigit;
}
