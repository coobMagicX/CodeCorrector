public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2 || len > 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    }
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    final String countryStr;
    if (ch3 == '_') {
        if (len < 5 || len > 6) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), "", str.substring(len - 1));
    } else if (!Character.isUpperCase(ch3)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch4 = str.charAt(4);
    countryStr = String.valueOf(Character.toUpperCase(ch3)) + Character.toUpperCase(ch4);
    if (len == 5) {
        return new Locale(str.substring(0, 2), countryStr);
    }
    if (str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch6 = str.charAt(6);
    if (!Character.isUpperCase(ch6)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    return new Locale(str.substring(0, 2), countryStr, String.valueOf(Character.toUpperCase(ch6)));
}