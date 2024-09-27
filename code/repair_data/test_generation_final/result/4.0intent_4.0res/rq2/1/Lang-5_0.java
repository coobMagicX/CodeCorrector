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
        Locale locale = new Locale(str.toLowerCase());
        if (!isAvailableLocale(locale)) {
            throw new IllegalArgumentException("Locale not available: " + str);
        }
        return locale;
    }
    if (len < 5) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    if (ch3 == '_') {
        Locale locale = new Locale(str.substring(0, 2).toLowerCase(), "", str.substring(4));
        if (!isAvailableLocale(locale)) {
            throw new IllegalArgumentException("Locale not available: " + str);
        }
        return locale;
    }
    final char ch4 = str.charAt(4);
    if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 5) {
        Locale locale = new Locale(str.substring(0, 2).toLowerCase(), str.substring(3, 5).toUpperCase());
        if (!isAvailableLocale(locale)) {
            throw new IllegalArgumentException("Locale not available: " + str);
        }
        return locale;
    }
    if (len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    Locale locale = new Locale(str.substring(0, 2).toLowerCase(), str.substring(3, 5).toUpperCase(), str.substring(6));
    if (!isAvailableLocale(locale)) {
        throw new IllegalArgumentException("Locale not available: " + str);
    }
    return locale;
}