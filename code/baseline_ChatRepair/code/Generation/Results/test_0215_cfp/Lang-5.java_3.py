public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    // Split the string by '_'
    String[] parts = str.split("_", -1);
    String language = "";
    String country = "";
    String variant = "";

    switch (parts.length) {
        case 1:
            language = parts[0];
            if (language.length() > 2) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            break;
        case 2:
            language = parts[0];
            country = parts[1];
            break;
        case 3:
            language = parts[0];
            country = parts[1];
            variant = parts[2];
            break;
        default:
            throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Validate language and country codes
    if (!language.isEmpty() && (language.length() != 2 || !Character.isLowerCase(language.charAt(0)) || !Character.isLowerCase(language.charAt(1)))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!country.isEmpty() && (country.length() != 2 || !Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1)))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    return new Locale(language, country, variant);
}
