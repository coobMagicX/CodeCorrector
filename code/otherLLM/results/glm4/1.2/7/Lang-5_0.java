public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 4) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Check for underscore and country code
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    final char ch2 = str.charAt(2);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1) || ch2 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Check language and variant code
    String language;
    String variant;
    if (len == 4) {
        return new Locale(str.substring(0, 2), "", "");
    } else if (len < 8) {
        final char ch3 = str.charAt(3);
        final char ch4 = str.charAt(4);
        if (!Character.isUpperCase(ch3) || !Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        language = str.substring(0, 2);
        variant = "";
    } else if (len == 7) {
        final char ch5 = str.charAt(5);
        final char ch6 = str.charAt(6);
        if (!Character.isUpperCase(ch5) || !Character.isUpperCase(ch6)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        language = str.substring(0, 2);
        variant = str.substring(4, 6);
    } else if (len == 10) {
        final char ch7 = str.charAt(7);
        final char ch8 = str.charAt(8);
        final char ch9 = str.charAt(9);
        if (!Character.isUpperCase(ch7) || !Character.isUpperCase(ch8) || !Character.isUpperCase(ch9)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        language = str.substring(0, 2);
        variant = str.substring(4, 6) + str.substring(7);
    } else {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    return new Locale(language, "", variant);
}