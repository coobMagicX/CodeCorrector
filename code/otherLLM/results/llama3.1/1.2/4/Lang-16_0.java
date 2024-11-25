public static Integer parseToInteger(String input) {
    if (input == null || !isDigits(input)) {
        throw new NumberParsingException("Invalid integer: " + input);
    }
    try {
        return Integer.parseInt(input);
    } catch (NumberFormatException e) {
        throw new NumberParsingException("Invalid integer: " + input, e);
    }
}

public static Long parseToLong(String input) {
    if (input == null || !isDigits(input)) {
        throw new NumberParsingException("Invalid long: " + input);
    }
    try {
        return Long.parseLong(input);
    } catch (NumberFormatException e) {
        throw new NumberParsingException("Invalid long: " + input, e);
    }
}

private static boolean isDigits(String input) {
    for (char c : input.toCharArray()) {
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return true;
}