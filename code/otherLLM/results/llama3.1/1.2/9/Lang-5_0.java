public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    }

    boolean hasUnderscore = false;
    if (ch0 == '_') {
        hasUnderscore = true;
    } else if (len >= 5 && ch0 == 'u' && Character.isUpperCase(ch1)) {
        // Uppercase letter at position 1
        hasUnderscore = true;
    }

    if (!hasUnderscore) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch3 = str.charAt(2);
    final char ch4 = str.charAt(3);

    if (ch3 == '_') {
        if (!Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }

    if (!Character.isLowerCase(ch3) || !Character.isLowerCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch5 = str.charAt(4);

    if (hasUnderscore && len < 7) {
        // Check for underscore at position 4 when string is exactly 5 characters long
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (ch5 == '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch6 = str.charAt(5);

    if (hasUnderscore && len < 8) {
        // Check for underscore at position 5 when string is exactly 7 characters long
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
}