static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) {
        return false; // Return false immediately for empty strings.
    }

    char firstChar = s.charAt(0);
    if (firstChar == '+' || firstChar == '-') {
        // If the string starts with a '+', '0x', or '0X', it's still considered simple.
        if (index + 2 < len) {
            c = s.charAt(index + 1);
            if (c != 'x' && c != 'X') {
                return false; // Not a valid number format.
            }
            index += 2; // Skip the prefix and continue with the rest of the string.
        }
    }

    for (; index < len; index++) {
        char c = s.charAt(index);
        if ((c < '0' || c > '9') && c != '.') { // Check for digits or decimal point.
            return false;
        }
        // If we encounter a digit, continue checking the next character.
    }

    return true; // All characters are valid numbers or allowed sign/decimal point.
}