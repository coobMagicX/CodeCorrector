static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Check for an optional leading '+' or '-'
    if (len == 0 || (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '+' && s.charAt(0) != '-')) {
        return false;
    }
    
    boolean hasDecimalPoint = false;
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c == '.') {
            // Check for a single decimal point and make sure it's not at the start or end
            if (hasDecimalPoint || index == 0 || index == len - 1) {
                return false;
            }
            hasDecimalPoint = true;
        } else if (!Character.isDigit(c) && c != 'x' && c != 'X') {
            // Allow hexadecimal representation starting with '0x' or '0X'
            if (index > 0 && s.charAt(index - 1) == '0' && (c == 'x' || c == 'X')) {
                continue; // Skip the check for digits in this case
            }
            return false;
        }
    }

    // For hexadecimal numbers, the rest of the string after "0x" or "0X" must be valid hex characters
    if (hasDecimalPoint && !Character.isDigit(s.charAt(len - 1))) {
        return false; // Decimal number cannot end with a non-digit character
    }
    
    return true;
}