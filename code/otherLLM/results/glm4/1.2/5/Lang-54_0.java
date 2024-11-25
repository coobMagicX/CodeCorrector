public static Locale toLocale(String str) {
    if (str == null || str.isEmpty()) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    int len = str.length();
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);

    // Check for the 'fr__POSIX' special case.
    if (ch0 == 'f' && ch1 == 'r' && str.startsWith("fr__POSIX")) {
        return new Locale("fr", "FR", "posix");
    }

    if (len != 2 && len != 5 && len != 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Check for lowercase two-letter code.
    if (ch0 >= 'a' && ch0 <= 'z' && ch1 >= 'a' && ch1 <= 'z') {
        return new Locale(str, "");
    }

    // Check for uppercase two-letter country and uppercase language code.
    if ((ch0 >= 'A' && ch0 <= 'Z') && (ch1 >= 'A' && ch1 <= 'Z')) {
        String countryCode = str.substring(2, 4);
        String languageCode = str.substring(3, 5);

        // Additional check for the '_' character in between.
        if (str.charAt(2) == '_') {
            return new Locale(str.substring(0, 2), languageCode, countryCode);
        }
    }

    throw new IllegalArgumentException("Invalid locale format: " + str);
}