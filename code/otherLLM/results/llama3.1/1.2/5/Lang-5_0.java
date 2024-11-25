public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 3 || str.startsWith("_") && len == 3) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch2 = str.charAt(2);
    if (ch2 != '_' && (len < 4 || str.startsWith("_", 3))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!Character.isLetter(ch0) || !Character.isLetter(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (str.startsWith("_") && len == 3) {
        return new Locale(str.substring(0, 2), "", "");
    } else if (len == 3) {
        return new Locale(str, "", "");
    } else {
        final char ch3 = str.charAt(3);
        if (ch3 != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (!Character.isUpperCase(ch0)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(4));
    }
}