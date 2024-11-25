public static Locale toLocale(final String str) {
    if (str == null || str.length() < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final int len = str.length();
    if (!Character.isLowerCase(str.charAt(0)) || !Character.isLowerCase(str.charAt(1))) {
        throw new IllegalArgumentException("Locale code must start with two lowercase letters: " + str);
    }
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    if (!Character.isUpperCase(ch3)) {
        throw new IllegalArgumentException("Third character after underscore must be uppercase: " + str);
    }
    if (str.charAt(4) == '_') {
        return new Locale(str.substring(0, 2), "", str.substring(5));
    } else if (len == 6) {
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Fourth character after underscore must be uppercase: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), "");
    } else if (str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    } else {
        final char ch6 = str.charAt(6);
        if (!Character.isUpperCase(ch6)) {
            throw new IllegalArgumentException("Sixth character after second underscore must be uppercase: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(7));
    }
}