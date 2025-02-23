public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }

    // Break the input string into possible parts based on underscores
    String[] parts = str.split("_", -1);
    if (parts.length > 3) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Ensuring the language part consists of exactly two lowercase letters
    String language = parts[0];
    if (language.length() != 2 || !language.toLowerCase().equals(language) || !language.matches("[a-z]{2}")) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Country code validity checks
    String country = "";
    if (parts.length > 1) {
        country = parts[1];
        if (country.length() != 0 && (country.length() != 2 || !country.toUpperCase().equals(country) || !country.matches("[A-Z]{2}"))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    // If there's a variant, include it
    String variant = parts.length > 2 ? parts[2] : "";

    // Return the appropriate Locale object based on the inputs
    return new Locale(language, country, variant);
}
