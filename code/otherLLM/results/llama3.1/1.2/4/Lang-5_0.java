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
        // Correctly handle strings with length 3 or more starting with an underscore
        if (len >= 5 && len < 7 && str.substring(0, 4).equals("_" + ch3)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 3) {
            // Check for invalid input when the string is at least 5 characters long and starts with an underscore
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale(str.substring(0, 2), "", "");
        } else {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }
    final char ch3 = str.charAt(3);
    if (ch3 == '_') {
        // Check that characters after the underscore are letters (not numbers)
        for (int i = 4; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        }
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }
    final char ch4 = str.charAt(4);
    if (ch3 == '_' && !Character.isUpperCase(ch4)) {
        // Check that characters after the underscore are letters (not numbers)
        for (int i = 5; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        }
        return new Locale(str.substring(0, 2), str.substring(3, 4), str.substring(5));
    } else {
        // Check that characters after the underscore are letters (not numbers)
        for (int i = 4; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    }
}