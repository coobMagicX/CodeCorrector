public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }

    str = str.trim(); // remove whitespace

    // Check for a single character version of the string that isn't just a sign.
    if ((str.length() == 1) && !Character.isDigit(str.charAt(0))) {
        return false;
    }

    int index = (str.charAt(0) == '-') ? 1 : 0;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean foundDigit = false;

    for (; index < str.length(); index++) {
        char ch = str.charAt(index);
        
        if (Character.isDigit(ch)) {
            foundDigit = true;
        } else if (ch == '.') {
            if (hasDecPoint || hasExp) { // Decimal points are not valid after exponents or a second decimal point
                return false;
            }
            hasDecPoint = true;
        } else if (ch == 'e' || ch == 'E') {
            if (hasExp) { // Ensure only one instance of 'e'
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            foundDigit = false; // reset for the requirement of digits after 'e'
        } else if (ch == '+' || ch == '-') {
            // Sign can occur after e/E
            if (!(str.charAt(index - 1) == 'e' || str.charAt(index - 1) == 'E')) {
                return false;
            }
        } else {
            // Invalid character found
            return false;
        }
    }

    return foundDigit;  // At least one digit is necessary for a valid number.
}
