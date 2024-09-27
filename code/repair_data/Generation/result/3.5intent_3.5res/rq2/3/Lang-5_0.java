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
    if (!Character.isLowerCase(ch0) && !Character.isUpperCase(ch0)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!Character.isLowerCase(ch1) && !Character.isUpperCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    }
    if (len < 5) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.charAt(2) != '_' && str.charAt(2) != '-') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    if (ch3 == '_' || ch3 == '-') {
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }
    if (!Character.isUpperCase(ch3)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch4 = str.charAt(4);
    if (!Character.isUpperCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 5) {
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    }
    if (len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.charAt(5) != '_' && str.charAt(5) != '-') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
}