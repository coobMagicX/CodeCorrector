public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }

    String[] parts = str.split("_", -1);  // Split with limit -1 to retain empty trailing parts

    // Assign the parts to language, country, and variant based on their positions
    String language = (parts.length > 0) ? parts[0] : "";
    String country = (parts.length > 1) ? parts[1] : "";
    String variant = (parts.length > 2) ? parts[2] : "";

    // Validation conditions according to locale formatting
    if (language.isEmpty() && !country.isEmpty() && parts[0].length() == 0) {
        // When language code is absent, and country code is present
        if (country.length() != 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    } else if (!language.isEmpty() && (language.length() != 2 || !Character.isLowerCase(language.charAt(0)) || !Character.isLowerCase(language.charAt(1)))) {
        // When language code is present and incorrectly formatted
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (!country.isEmpty() && (country.length() != 2 || !Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1)))) {
        // Invalid format for country code
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    return new Locale(language, country, variant);
}
