static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Check for negative numbers or a decimal point at the beginning
    if ((len == 1 && s.charAt(0) == '-') || (s.charAt(0) == '-' && s.charAt(1) == '.')) {
        return false;
    }

    boolean hasDecimalPoint = false;
    boolean hasExponent = false;
    
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        
        // Check for digits
        if (c < '0' || c > '9') {
            // Allow decimal point only once and after a digit
            if (c == '.' && !hasDecimalPoint) {
                hasDecimalPoint = true;
                // Check for exponent part of the number
                if (index + 1 < len && s.charAt(index + 1) == 'e' || s.charAt(index + 1) == 'E') {
                    if (index > 0 && !hasExponent) { // Only allow one exponent
                        hasExponent = true;
                    } else {
                        return false; // More than one exponent or invalid position of exponent
                    }
                }
            } else if ((c == 'e' || c == 'E') && (index + 1 < len)) {
                // Handle the case where 'e'/'E' is the last character
                hasExponent = true;
                continue; // Skip checking for digits after exponent
            } else {
                return false; // Invalid characters found
            }
        }
    }

    // Check if there is an exponent part but no digits following it
    if (hasExponent && len == s.indexOf('e') + 1) {
        return false;
    }

    return true;
}