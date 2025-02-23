public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    
    // Split by underscores to separate language, country, and variant
    String[] parts = str.split("_", -1);
    String language = parts.length > 0 ? parts[0] : "";
    String country = parts.length > 1 ? parts[1] : "";
    String variant = parts.length > 2 ? parts[2] : "";
    
    // Validate the language and country code using the length
    if (language.length() != 2 || !language.matches("[a-zA-Z]{2}")) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!country.isEmpty() && (country.length() != 2 || !country.matches("[A-Z]{2}"))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Construct the locale with the appropriate parts
    if (parts.length == 2) {
        return new Locale(language, country);
    } else if (parts.length >= 3) {
        // Concatenate additional parts that might represent extended variant codes
        StringBuilder variantBuilder = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (i > 2) {
                variantBuilder.append('_');
            }
            variantBuilder.append(parts[i]);
        }
        variant = variantBuilder.toString();
        return new Locale(language, country, variant);
    } else {
        return new Locale(language);
    }
}
