static boolean isSimpleNumber(String s) {
    // Trim the string to remove leading and trailing whitespaces
    s = s.trim();
    
    int len = s.length();
    if (len == 0) { // Check for empty string after trimming
        return false;
    }
    
    // Check for single zero which is a valid simple number
    if (len == 1 && s.charAt(0) == '0') {
        return true;
    }
    
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    
    // Check if the number does not start with zero unless it's the only digit
    if (s.charAt(0) == '0' && len > 1) {
        return false;
    }
    
    return true;
}