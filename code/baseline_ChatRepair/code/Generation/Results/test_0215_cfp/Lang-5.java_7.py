public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }

    // Check if the string starts with an underscore, indicating an omitted language
    if (str.startsWith("_")) {
        int secondUnderscoreIndex = str.indexOf('_', 1);
        if (secondUnderscoreIndex == -1) {
            // Only country is provided
            if (str.length() < 4) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            String country = str.substring(1, 3);
            if (!Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1))) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale("", country);
        } else {
            // Country and variant are provided
            String country = str.substring(1, secondUnderscoreIndex);
            if (!Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1))) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            String variant = str.substring(secondUnderscoreIndex + 1);
            return new Locale("", country, variant);
        }
    }

    // Parsing standard locale strings with language
    final int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    String language = str.substring(0, 2);
    
    if (!Character.isLowerCase(language.charAt(0)) || !Character.isLowerCase(language.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    if (len == 2) {
        return new Locale(language);
    }
    
    if (len < 5 || str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    String country = str.substring(3, 5);
    if (!Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    if (len == 5) {
        return new Locale(language, country);
    }
    
    if (len < 7 || str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    String variant = str.substring(6);
    return new Locale(language, country, variant);
}
