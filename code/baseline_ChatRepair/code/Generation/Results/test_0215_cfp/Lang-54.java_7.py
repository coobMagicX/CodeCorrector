public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 2) {
        return new Locale(str);
    }

    // Check if there is supposed to be more after the language code
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Look for the country code
    int countryIndex = 3;
    String country = "";
    if (len > 3 && str.charAt(countryIndex) != '_') {
        // We expect at least one more character for the country code
        if (countryIndex + 1 < len) {
            char ch3 = str.charAt(countryIndex);
            char ch4 = str.charAt(countryIndex + 1);
            if (ch3 >= 'A' && ch3 <= 'Z' && ch4 >= 'A' && ch4 <= 'Z') {
                country = str.substring(countryIndex, countryIndex + 2);
                countryIndex += 2;
            } else {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        } else {
            // Country code too short
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }
    
    // Check if there's a variant code
    String variant = "";
    if (countryIndex < len) {
        if (str.charAt(countryIndex) == '_') {
            variant = str.substring(countryIndex + 1);
        } else {
            // Expected underscore as separator before variant
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    return new Locale(str.substring(0, 2), country, variant);
}
