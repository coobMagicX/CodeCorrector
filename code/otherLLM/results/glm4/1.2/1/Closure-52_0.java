static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Check for hexadecimal prefix "0x" or "0X"
    if (len > 1 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
        // Handle the case where only "0x" or "0X" is present
        return len == 2;
    }

    // Check for decimal point for floating-point numbers
    int decimalPointIndex = -1;
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if ((c < '0' || c > '9') && c != '.') {
            return false;
        }
        // If a decimal point is found, verify the following character is not another decimal point
        if (c == '.' && (decimalPointIndex != -1 || index == len - 1)) {
            return false; // Multiple decimal points or at the end of the string without digits after
        }
        if (c == '.') {
            decimalPointIndex = index;
        }
    }

    // Check for a valid floating-point number or an integer with a decimal point at the end
    if (decimalPointIndex != -1) {
        return true; // Floating-point number is valid
    } else {
        return len > 0; // Integer number is valid
    }
}