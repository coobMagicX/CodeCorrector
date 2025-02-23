public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }

    // Splitting the input on "_" to differentiate segments easier
    String[] parts = str.split("_", -1); // -1 limit to preserve trailing empty strings
    int len = parts.length;
    String language = parts[0];
    String country = (len > 1) ? parts[1] : "";
    String variant = (len > 2) ? parts[2] : "";

    if (language.length() != 2 || (len > 1 && country.length() != 2 && !country.isEmpty())) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    for (int i = 0; i < language.length(); i++) {
        char ch = language.charAt(i);
        if (ch < 'a' || ch > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    for (int i = 0; i < country.length(); i++) {
        char ch = country.charAt(i);
        if (ch < 'A' || ch > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }
    
    // Correcting the method to handle language, country, and variant more accurately
    return new Locale(language, country, variant);
}
