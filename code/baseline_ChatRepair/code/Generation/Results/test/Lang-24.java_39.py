public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
  
    // controls the phase: 0=start, 1=integer part, 2=fraction part, 3=exponent part
    int phase = 0;
    boolean seenDigit = false;
    boolean allowSigns = false;

    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);

        switch(phase) {
            case 0: // Starting phase: optional sign and check for hexadecimal
                if (c == '+' || c == '-') {
                    allowSigns = false;
                    phase = 1; // move to integer part
                } else if (Character.isDigit(c)) {
                    phase = 1; // integer part
                    seenDigit = true;
                } else if (c == '.') {
                    phase = 2; // fraction part
                } else {
                    return false;
                }
                break;
            case 1: // Integer part (could go to fraction part or exponent).
                if (Character.isDigit(c)) {
                    seenDigit = true;
                } else if (c == '.') {
                    phase = 2; // move to fraction part
                } else if (c == 'e' || c == 'E') {
                    if (!seenDigit) return false;
                    phase = 3; // move to exponent part
                    seenDigit = false; // reset for exponent digits
                    allowSigns = true;
                } else {
                    return false;
                }
                break;
            case 2: // Fraction part: can only go to exponent
                if (Character.isDigit(c)) {
                    seenDigit = true;
                } else if (c == 'e' || c == 'E') {
                    if (!seenDigit) return false;
                    phase = 3; // move to exponent part
                    seenDigit = false; // reset for exponent digits
                    allowSigns = true;
                } else {
                    return false;
                }
                break;
            case 3: // Exponent part
                if (Character.isDigit(c)) {
                    seenDigit = true;
                } else if ((c == '+' || c == '-') && allowSigns) {
                    allowSigns = false; // only one sign after 'e' or 'E'
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }
    }

    return seenDigit; // must end with a digit (post any initial state or after exponent)
}
