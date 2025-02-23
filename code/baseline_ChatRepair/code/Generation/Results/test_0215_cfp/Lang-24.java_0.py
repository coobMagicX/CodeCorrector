public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    str = str.trim(); // Trim the string to remove leading/trailing white spaces

    int length = str.length();
    boolean hasDecimal = false;
    boolean hasExponent = false;
    boolean hasSign = false;
    boolean numberStarted = false;

    for (int i = 0; i < length; i++) {
        char ch = str.charAt(i);
        if (ch >= '0' && ch <= '9') {
            numberStarted = true;
            hasSign = false; // Once a number starts, signs should not follow immediately
        } else if (ch == '.') {
            if (hasDecimal || hasExponent) {
                return false;
            }
            hasDecimal = true;
        } else if (ch == 'e' || ch == 'E') {
            if (hasExponent || !numberStarted) {
                return false;
            }
            hasExponent = true;
            hasSign = true;  // E can be followed by a sign (+ or -)
            numberStarted = false; // Reset for the exponent part.
        } else if (ch == '+' || ch == '-') {
            if (!hasSign || !numberStarted) {
                return false;
            }
            hasSign = true;
            numberStarted = false; // Reset number state
        } else {
            // Invalid character
            return false;
        }
    }

    // If we reached here, and the last character state was numberStarted, it's a valid numeric format.
    return numberStarted;
}
