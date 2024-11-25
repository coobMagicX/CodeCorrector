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
        return new Locale(str);
    }
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final int countryCodeLen = getCountryCodeLength(str);
    if (countryCodeLen < 1 || countryCodeLen > 3) {
        throw new IllegalArgumentException("Invalid country code length in locale format: " + str);
    }
    for (int i = 3; i <= len - 2 && i < 6; i++) {
        final char ch = str.charAt(i);
        if (!Character.isLetter(ch)) {
            throw new IllegalArgumentException("Invalid character in locale format: " + str);
        }
    }
    String countryCode;
    if (countryCodeLen == 3) {
        final char ch2 = str.charAt(3);
        final char ch4 = str.charAt(5);
        if (!Character.isUpperCase(ch2) || !Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid country code in locale format: " + str);
        }
        countryCode = str.substring(3, 6);
    } else {
        final int start = str.indexOf("_", 2);
        if (start == -1) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final String languageCode = str.substring(0, start);
        countryCode = str.substring(start + 1, len - 1);
    }
    return new Locale(str.substring(0, 2), countryCode, "");
}

private static int getCountryCodeLength(final String str) {
    if (str.indexOf("_", 2) == -1) {
        return -1;
    }
    final char ch = str.charAt(3);
    for (; ch == '_'; ch = str.charAt(++len)) {}
    return len - 3;
}