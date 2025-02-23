public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Initial parsing variables
    String language = "";
    String country = "";
    String variant = "";

    // Analyzing components separated by underscores
    String[] parts = str.split("_", -1); // Limit -1 to include trailing empty strings

    // Validate parts and assign properly
    switch (parts.length) {
        case 1:
            language = parts[0];
            break;
        case 2:
            if (str.startsWith("_")) {
                country = parts[1];
            } else {
                language = parts[0];
                country = parts[1];
            }
            break;
        case 3:
            if (str.startsWith("_")) {
                country = parts[1];
                variant = parts[2];
            } else {
                language = parts[0];
                country = parts[1];
                variant = parts[2];
            }
            break;
        default:
            throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Validate language, country, and length specifics
    if (str.startsWith("_") && (str.length() < 5 || country.isEmpty() || (parts.length > 2 && variant.isEmpty()))) {
        throw new IllegalArgumentException("Must be at least 5 chars if starts with underscore");
    }
    if ((!language.isEmpty() && language.length() != 2) || (!country.isEmpty() && country.length() != 2)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Create and return the locale
    return new Locale(language, country, variant);
}
