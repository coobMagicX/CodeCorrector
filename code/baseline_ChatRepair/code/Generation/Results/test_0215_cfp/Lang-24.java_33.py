public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }
    
    // trim the string to allow checks without leading/trailing spaces
    str = str.trim();
    
    // Check hexadecimal numbers
    if (str.startsWith("0x") || str.startsWith("-0x") || str.startsWith("+0x")) {
        int start = str.startsWith("-") || str.startsWith("+") ? 3 : 2;
        if (str.length() == start) 
            return false; // string is "0x", "+0x" or "-0x" without numbers
        for (int i = start; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                return false;
            }
        }
        return true; // valid hexadecimal
    }

    // Check scientific and decimal numbers
    boolean hasDecimal = false;
    boolean hasExponent = false;
    boolean validNumberSeen = false;
    
    // First character could be a sign, skip if it's just a single character with '+' or '-'
    int start = (str.charAt(0) == '-' || str.charAt(0) == '+') ? 1 : 0;
    if (start == 1 && str.length() == 1) 
        return false; // String is "+" or "-"
    
    for (int i = start; i < str.length(); i++) {
        char c = str.charAt(i);
        if (c >= '0' && c <= '9') {
            validNumberSeen = true;
        } else if (c == '.') {
            if (hasExponent || hasDecimal) 
                return false; // can't have a decimal point in an exponent or multiple decimals
            hasDecimal = true;
        } else if (c == 'e' || c == 'E') {
            if (!validNumberSeen || hasExponent) 
                return false; // no digits before E or multiple E's
            hasExponent = true;
            validNumberSeen = false; // Need at least one digit after the exponent
            // The next character can be a sign, adjust index if it is
            if (i + 1 < str.length() && (str.charAt(i + 1) == '-' || str.charAt(i + 1) == '+')) {
                i++; // skip the sign
            }
        } else {
            return false; // invalid character for numbers
        }
    }
    
    return validNumberSeen; // at least one numeric digit must appear in a valid number
}
