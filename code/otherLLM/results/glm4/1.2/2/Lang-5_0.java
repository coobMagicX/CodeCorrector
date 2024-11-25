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
        return new Locale(str, "", "");
    } else if (len == 5) {
        final char ch3 = str.charAt(2);
        final char ch4 = str.charAt(3);
        if (ch3 != '_' || !Character.isUpperCase(ch4)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), "");
    } else if (len == 7) {
        final char ch3 = str.charAt(2);
        final char ch4 = str.charAt(3);
        final char ch5 = str.charAt(4);
        final char ch6 = str.charAt(5);
        if (ch3 != '_' || !Character.isUpperCase(ch4) || ch5 != '_' || !Character.isUpperCase(ch6)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
    } else {
        // This should never be reached due to the initial len check.
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
}