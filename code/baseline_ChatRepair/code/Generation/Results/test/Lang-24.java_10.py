public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    // trim any leading/trailing whitespace
    str = str.trim();
    if (str.length() == 0) {
        return false;
    }
    
    char[] chars = str.toCharArray();
    int index = 0;
    int last = chars.length - 1;
    
    // handle negative sign if present
    if (chars[index] == '-') {
        if (chars.length == 1) {
            return false;
        }
        index++;
    }
    
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean hasDigit = false;
    
    while (index <= last) {
        char ch = chars[index];
        if (Character.isDigit(ch)) {
            hasDigit = true;
        } else if (ch == '.') {
            // only one decimal point, and not allowed in the exponent
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (ch == 'e' || ch == 'E') {
            // only one exponent, must follow a digit
            if (hasExp || !hasDigit) {
                return false;
            }
            hasExp = true;

            // exponent must be followed by plus, minus or a digit
            if (index == last || (!Character.isDigit(chars[index + 1]) && 
                                    (chars[index + 1] != '+' && chars[index + 1] != '-'))) {
                return false;
            }
        } else if (ch == '+' || ch == '-') {
            // '+' or '-' must immediately follow the exponent
            if (!(chars[index - 1] == 'e' || chars[index - 1] == 'E')) {
                return false;
            }
        } else {
            return false;
        }
        index++;
    }
    
    return hasDigit; // ensure the string contains at least one digit
}
