public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 2) {
        return new Locale(str);
    }

    if (len == 3 || len == 4 || len == 6) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Begin processing beyond the language
    String[] parts = str.substring(3).split("_", -1);
    
    String country = "";
    String variant = "";

    if (parts.length > 0 && parts[0].length() == 2) {
        country = parts[0];
        // Check characters of country are uppercase
        if (!country.equals(country.toUpperCase()) || !country.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    if (parts.length > 1) {
        variant = parts[1];
    }

    if (parts.length > 2) {
        // Reconstruct full variant if there are more than two underscores
        variant = String.join("_", Arrays.copyOfRange(parts, 1, parts.length));
    }

    return new Locale(str.substring(0, 2), country, variant);
}
