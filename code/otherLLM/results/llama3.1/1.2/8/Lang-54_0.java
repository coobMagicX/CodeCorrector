public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }

    // Enhanced input format validation
    int len = str.length();
    if (!isValidLocaleFormat(len)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Handle variations in locale representation
    String languageCode;
    String countryOrVariantCode;
    if (len == 2) {
        return new Locale(str, "");
    } else {
        languageCode = str.substring(0, 2);

        char separator = '_';
        if (str.charAt(2) != separator || str.charAt(len - 1) != separator) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }

        countryOrVariantCode = str.substring(3, len - 1);

        // Enhanced error message for invalid input
        if (!isValidCountryOrVariantCode(countryOrVariantCode)) {
            throw new IllegalArgumentException(String.format("Invalid country or variant code: %s", countryOrVariantCode));
        }
    }

    return new Locale(languageCode, countryOrVariantCode);
}

// Helper method to validate locale format
private static boolean isValidLocaleFormat(int len) {
    return (len == 2 || len == 5 || len >= 7 && len <= 9);
}

// Helper method to validate country or variant code
private static boolean isValidCountryOrVariantCode(String code) {
    if (code.length() < 1 || code.length() > 3) {
        return false;
    }
    for (int i = 0; i < code.length(); i++) {
        char ch = code.charAt(i);
        if (ch < 'A' || ch > 'Z') {
            return false;
        }
    }
    return true;
}