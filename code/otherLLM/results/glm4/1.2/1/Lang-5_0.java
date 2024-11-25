public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    
    // Check for underscore at the start
    if (!str.startsWith("_")) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final int len = str.length();
    if (len < 3) { // At least 2 characters before underscore and at least 1 after
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    final char ch2 = str.charAt(2);

    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Check the third character is an underscore
    if (ch2 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 3) { // Simple format, e.g., "en"
        return new Locale(str.substring(1));
    }

    if (len < 6) { // At least 5 characters to form the full locale
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch3 = str.charAt(3);
    final char ch4 = str.charAt(4);

    if (!Character.isUpperCase(ch3)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Check for underscore after the country code
    if (ch4 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 5) { // Format with language and country, e.g., "en_US"
        return new Locale(str.substring(1, 3), str.substring(4));
    }

    if (len < 8) { // At least 7 characters to form the locale with variant
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    final char ch5 = str.charAt(5);

    if (!Character.isUpperCase(ch5)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // All checks are done, construct and return the Locale object
    return new Locale(str.substring(1, 3), str.substring(4, 6), str.substring(7));
}