static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Check for an optional sign at the beginning
    if (s.charAt(0) == '+' || s.charAt(0) == '-') {
        s = s.substring(1); // Remove the sign for processing the rest of the string
    }
    
    int lenAfterSign = s.length();
    boolean hasFractionPart = false;
    boolean hasExponentPart = false;

    if (lenAfterSign > 0) {
        char firstChar = s.charAt(0);

        // Check for hexadecimal representation
        if (firstChar == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
            s = s.substring(2); // Remove the '0x' or '0X' prefix
            lenAfterSign -= 2;
        }
    }

    for (int index = 0; index < lenAfterSign; index++) {
        char c = s.charAt(index);
        if (c == '.') { // Check for a decimal point
            hasFractionPart = true;
        } else if (c == 'e' || c == 'E') { // Check for an exponent part
            if (!hasExponentPart) { // Only allow one exponent character in the string
                hasExponentPart = true;
                if ((index + 1) < lenAfterSign && (s.charAt(index + 1) == '+' || s.charAt(index + 1) == '-')) {
                    index++; // Skip the sign for the exponent part
                }
            } else {
                return false; // Multiple 'e' or 'E' characters are not allowed
            }
        } else if (c < '0' || c > '9') {
            // Check for hexadecimal digits
            if ((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                continue; // Hexadecimal characters are allowed
            } else {
                return false;
            }
        }
    }

    return true; // Passed all checks, the string is a simple number
}