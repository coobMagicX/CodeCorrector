static boolean isSimpleNumber(String s) {
    int len = s.length();

    // Check for '0' or '0x' prefix to allow octal or hexadecimal numbers.
    if (len > 1 && (s.charAt(0) == '0')) {
        if (s.length() > 2 && s.charAt(1) == 'x') { // Hexadecimal check
            return isHexNumber(s.substring(2));
        } else if (isOctalNumber(s.substring(1))) { // Octal check
            return true;
        }
    }

    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);

        // Allow digits, decimal point in non-octal/hexadecimal contexts,
        // and potentially more characters depending on further context.
        if (!Character.isDigit(c) && c != '.') {
            return false;
        }

        // If the first character is a digit other than '0', it's not octal/hexadecimal.
        if (index == 0 && c > '0' && s.length() > 1 && (s.charAt(1) == '.' || s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
            return false;
        }
    }

    // Handle floating-point number check.
    if (len > 1 && s.indexOf('.') != -1) {
        int decimalPointIndex = s.indexOf('.');
        // Check for exponent in the form of e or E followed by digits.
        int expIndex = s.toLowerCase().indexOf('e');
        if (expIndex != -1) {
            return isExponentForm(s, decimalPointIndex, expIndex);
        }
    }

    return true;
}

private static boolean isHexNumber(String hex) {
    for (int i = 0; i < hex.length(); i++) {
        char c = hex.charAt(i);
        if (!Character.isDigit(c) && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
            return false;
        }
    }
    return true;
}

private static boolean isOctalNumber(String octal) {
    for (int i = 0; i < octal.length(); i++) {
        char c = octal.charAt(i);
        if (c < '0' || c > '7') {
            return false;
        }
    }
    return true;
}

private static boolean isExponentForm(String number, int decimalPointIndex, int expIndex) {
    // Check for exponent before or after the decimal point.
    if ((expIndex < decimalPointIndex && (expIndex == 0 || !Character.isDigit(number.charAt(expIndex - 1)))) ||
        (expIndex > decimalPointIndex && (expIndex + 1 >= number.length() || !Character.isDigit(number.charAt(expIndex + 1))))) {
        return false;
    }

    // Check the rest of the string after the exponent.
    String exponentPart = number.substring(expIndex + 1);
    for (int i = 0; i < exponentPart.length(); i++) {
        if (!Character.isDigit(exponentPart.charAt(i))) {
            return false;
        }
    }

    return true;
}