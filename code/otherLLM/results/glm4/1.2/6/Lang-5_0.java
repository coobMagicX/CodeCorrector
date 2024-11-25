public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2 || len > 7) { // Correct the range check for valid lengths
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (!Character.isLowerCase(str.charAt(0)) || !Character.isLowerCase(str.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 2) { // Simple case with two lowercase letters
        return new Locale(str, "", "");
    } else if (len >= 5 && len <= 7) { // Cases with more than two characters
        final char ch2 = str.charAt(2);
        if (ch2 != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str); // Check for underscore at position 3
        }

        if (!Character.isUpperCase(str.charAt(3)) || !Character.isUpperCase(str.charAt(4))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }

        if (len == 5) { // Three-element locale string, no country code
            return new Locale(str.substring(0, 2), str.substring(3, 5), "");
        } else { // Five or seven-character locale string with country code
            final char ch6 = (len == 7) ? str.charAt(6) : '_';
            if (!Character.isUpperCase(ch6)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return len == 5 ?
                   new Locale(str.substring(0, 2), str.substring(3, 5), "") :
                   new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
        }
    }

    // This else case is technically unreachable due to the range check at the beginning
    throw new IllegalArgumentException("Invalid locale format: " + str);
}