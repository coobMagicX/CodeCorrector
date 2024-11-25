static boolean isSimpleNumber(String s) {
    // Trim leading and trailing whitespaces to handle optional whitespace
    s = s.trim();
    
    // Check for hexadecimal prefix
    if (s.startsWith("0x") || s.startsWith("0X")) {
        return isHexadecimal(s.substring(2));
    }
    
    // Check for decimal numbers which may include a decimal point
    return isDecimal(s);
}

private static boolean isHexadecimal(String s) {
    int len = s.length();
    if (len == 0) return false;
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!(c >= '0' && c <= '9') && !(c >= 'a' && c <= 'f') && !(c >= 'A' && c <= 'F')) {
            return false;
        }
    }
    return true;
}

private static boolean isDecimal(String s) {
    int len = s.length();
    if (len == 0 || s.equals(".")) return false; // Do not accept just a decimal point
    
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!(c >= '0' && c <= '9') && c != '.') { // Allow decimal points within the number
            return false;
        }
    }
    return true;
}