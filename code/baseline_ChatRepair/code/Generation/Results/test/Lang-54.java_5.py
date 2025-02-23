public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    String[] parts = str.split("_", -1);
    String language = parts.length > 0 ? parts[0] : "";
    String country = parts.length > 1 ? parts[1] : "";
    String variant = parts.length > 2 ? parts[2] : "";

    // Validate the language and country code lengths
    if (language.length() != 2 || (country.length() != 0 && country.length() != 2)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Validate character values for language and country
    for (int i = 0; i < language.length(); i++) {
        if (language.charAt(i) < 'a' || language.charAt(i) > 'z') {
            throw new IllegalArgumentException("Invalid language code format: " + str);
        }
    }
    for (int i = 0; i < country.length(); i++) {
        if (country.charAt(i) < 'A' || country.charAt(i) > 'Z') {
            throw new IllegalArgumentException("Invalid country code format: " + str);
        }
    }

    // Rebuild the variant if it was split by underscores
    if (parts.length > 3) {
        StringBuilder variantBuilder = new StringBuilder(variant);
        for (int i = 3; i < parts.length; i++) {
            if (i > 3) variantBuilder.append('_');
            variantBuilder.append(parts[i]);
        }
        variant = variantBuilder.toString();
    }

    return new Locale(language, country, variant);
}
