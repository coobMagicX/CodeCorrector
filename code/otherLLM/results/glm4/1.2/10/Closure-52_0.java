static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Handle the hexadecimal prefix '0x' or '0X'
    if (len >= 2 && (s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X'))) {
        return isHexadecimal(s.substring(2));
    }
    
    // Handle floating-point numbers
    int dotIndex = s.indexOf('.');
    if (dotIndex != -1) {
        // Check for valid decimal point placement and range of digits before and after the decimal point
        int beforeDotLength = dotIndex;
        int afterDotLength = len - dotIndex - 1;
        return isDecimal(beforeDotLength, afterDotLength);
    }
    
    // If no dot is found and no '0x' or '0X', it's a simple integer
    return isDecimal(len, 0);
}

private static boolean isHexadecimal(String s) {
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9' || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
            return false;
        }
    }
    return true;
}

private static boolean isDecimal(int length, int dotPosition) {
    if (length == 0 || (dotPosition != 0 && dotPosition != length - 1)) {
        // A valid decimal number must not be empty and cannot have a decimal point at the first or last position
        return false;
    }

    for (int index = 0; index < length; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            // The character is neither a digit nor a valid decimal point
            return false;
        }
    }
    return true;
}