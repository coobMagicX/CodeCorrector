public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch0 = str.charAt(0);
    if (ch0 == '_') {
        // Specific handling for locales starting with an underscore
        if (len < 3) { // Too short to be valid after an underscore
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch1 = str.charAt(1);
        final char ch2 = str.charAt(2);
        if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 3) {
            return new Locale("", str.substring(1, 3));
        }
        if (str.charAt(3) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 4) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale("", str.substring(1, 3), str.substring(4));
    } else {
        if (!Character.isLowerCase(ch0)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str.toLowerCase());
        }
        if (len < 5) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch3 = str.charAt(3);
        if (ch3 == '_') {
            if (len == 4) {
                throw new IllegalArgumentException("Invalid locale format: " + str); // Ensure there is something after the second underscore
            }
            return new Locale(str.substring(0, 2).toLowerCase(), "", str.substring(4));
        }
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 5) {
            return new Locale(str.substring(0, 2).toLowerCase(), str.substring(3, 5));
        }
        if (len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(5) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2).toLowerCase(), str.substring(3, 5), str.substring(6));
    }
}