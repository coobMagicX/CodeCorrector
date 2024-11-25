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
    if (!Character.isLowerCase(ch0)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    } else if (ch1 == '_') {
        // Correct length validation for underscore-starting locales
        if (len < 5) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch3 = str.charAt(3);
        if (!Character.isUpperCase(ch3)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(4));
    } else if (len == 2) {
        return new Locale(str);
    } else if (!Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    } else if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    final char ch3 = str.charAt(3);
    if (ch3 == '_') {
        // Ensure correct parsing for underscore-separated values
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(5));
    } else if (len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    } else if (str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    final char ch4 = str.charAt(4);
    final char ch6 = str.charAt(6);
    if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4) || !Character.isUpperCase(ch6)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(str.substring(0, 2), str.substring(5));
}