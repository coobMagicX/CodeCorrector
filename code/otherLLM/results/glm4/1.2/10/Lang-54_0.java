public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len != 2 && len != 5 && len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    // Extract language code
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    // Language code is always 2 characters long
    String language = str.substring(0, 2);

    if (len == 2) {
        return new Locale(language, "");
    } else {
        // Extract country/region code
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch3 = str.charAt(3);
        char ch4 = str.charAt(4);
        if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        String country = str.substring(3, 5);

        // Check for variant and create Locale object
        if (len == 5) {
            return new Locale(language, country);
        } else {
            if (str.charAt(5) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch6 = str.charAt(6);
            if (ch6 < 'A' || ch6 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            String variant = str.substring(6);

            return new Locale(language, country, variant);
        }
    }
}