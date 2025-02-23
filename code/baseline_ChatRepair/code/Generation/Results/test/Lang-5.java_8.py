public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    // Split the string into parts divided by '_'
    String[] parts = str.split("_", -1);
    
    // Handle cases based on the number of parts found
    switch (parts.length) {
        case 1:
            // When there is only one part, it should be the language
            if (parts[0].length() != 2 || !isAlpha(parts[0]) || !isLowerCase(parts[0])) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale(parts[0]);
        case 2:
            // Two parts could be language_country or _country
            if (parts[0].isEmpty()) {
                if (parts[1].length() != 2 || !isAlpha(parts[1]) || !isUpperCase(parts[1])) {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale("", parts[1]);
            } else if (parts[0].length() == 2 && parts[1].length() == 2 && isAlpha(parts[0]) 
                && isLowerCase(parts[0]) && isAlpha(parts[1]) && isUpperCase(parts[1])) {
                return new Locale(parts[0], parts[1]);
            }
            throw new IllegalArgumentException("Invalid locale format: " + str);
        case 3:
            // Three parts can be language_country_variant or _country_variant
            if (parts[0].isEmpty()) {
                if (parts[1].length() != 2 || !isAlpha(parts[1]) || !isUpperCase(parts[1])) {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale("", parts[1], parts[2]);
            } else if (parts[0].length() == 2 && parts[1].length() == 2 && parts[2].length() > 0 
                && isAlpha(parts[0]) && isLowerCase(parts[0]) && isAlpha(parts[1]) && isUpperCase(parts[1])) {
                return new Locale(parts[0], parts[1], parts[2]);
            }
            throw new IllegalArgumentException("Invalid locale format: " + str);
        default:
            throw new IllegalArgumentException("Invalid locale format: " + str);
    }
}

private static boolean isAlpha(String s) {
    return s.chars().allMatch(Character::isLetter);
}

private static boolean isLowerCase(String s) {
    return s.chars().allMatch(Character::isLowerCase);
}

private static boolean isUpperCase(String s) {
    return s.chars().allMatch(Character::isUpperCase);
}
