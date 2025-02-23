public static boolean isNumber(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }
    str = str.trim();
    
    if (str.matches("-?0[xX][0-9a-fA-F]+")) {  // Check for hexadecimals
        return true;
    }

    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean hasSign = false;
    boolean foundDigit = false;

    // Check characters in the string
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if (Character.isDigit(c)) {
            foundDigit = true;
            hasSign = false;  // Reset sign flag if digit found
        } else if (c == '.') {
            if (hasDecPoint || hasExp) return false;
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            if (hasExp || !foundDigit) return false;
            hasExp = true;
            hasSign = true;  // Expect a sign or digit next
        } else if (c == '+' || c == '-') {
            if (!hasSign || i == 0 || !(str.charAt(i-1) == 'e' || str.charAt(i-1) == 'E')) return false;
            hasSign = true;
        } else {
            return false;  // Invalid character found
        }
    }

    // Final character should be a digit
    return foundDigit && !hasSign;
}
