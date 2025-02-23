public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int length = str.length();
    if (length < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Special handling when the locale starts with '_'
    if (str.charAt(0) == '_') {
        if (length < 5) {
            throw new IllegalArgumentException("Must be at least 5 chars if starts with underscore");
        }
        if (str.charAt(2) != '_' || !Character.isUpperCase(str.charAt(1)) || !Character.isUpperCase(str.charAt(3))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale("", str.substring(1, 3), str.substring(4));
    }

    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (length == 2) {
        return new Locale(str);
    }
    if (length < 5 || str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    if (ch3 == '_') {
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }
    final char ch4 = str.charAt(4);
    if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (length == 5) {
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    }
    if (length < 7 || str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
}
