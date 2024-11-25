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
        return new Locale(String.valueOf(ch0), String.valueOf(ch1));
    }
    if (len < 5) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch2 = str.charAt(2);
    if (ch2 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    final char ch4 = str.charAt(4);
    if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 5) {
        return new Locale(String.valueOf(ch0), String.valueOf(ch1), String.valueOf(ch3), String.valueOf(ch4));
    }
    if (len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch5 = str.charAt(5);
    if (ch5 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(String.valueOf(ch0), String.valueOf(ch1), String.valueOf(ch3), String.valueOf(ch4), "", str.substring(6));
}