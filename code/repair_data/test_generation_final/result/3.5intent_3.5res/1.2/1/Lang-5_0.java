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
    if (!Character.isLowerCase(ch0) || (!Character.isLowerCase(ch1) && ch0 != '_')) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    }
    if (len < 5) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.charAt(2) != '_' && ch1 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (ch1 == '_') {
        final char ch3 = str.charAt(2);
        if (!Character.isUpperCase(ch3)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 3) {
            return new Locale(str.substring(1, 3));
        }
        if (len < 6) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(3) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 6) {
            return new Locale(str.substring(1, 3), str.substring(4, 6));
        }
        if (len < 8) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(5) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(1, 3), str.substring(4, 6), str.substring(7));
    } else {
        final char ch3 = str.charAt(3);
        if (ch3 == '_') {
            return new Locale(str.substring(0, 2), "", str.substring(4));
        }
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        }
        if (len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(5) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch6 = str.charAt(6);
        if (!Character.isUpperCase(ch6)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 7) {
            return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6, 7));
        }
        if (len < 9) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(7) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
    }
}