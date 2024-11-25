public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    // Adjust length validation for 5-character and 7-character strings
    if ((len != 2 && len != 5 && len != 7) || (len == 5 && str.charAt(2) != '_' && str.charAt(3) != '_') || (len == 7 && str.charAt(5) != '_')) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Adjust character range checks
    if (!Character.isLowerCase(str.charAt(0)) || !Character.isLowerCase(str.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    int start = 0;
    int end = 2; // For ISO 639-1 language code followed by an underscore and ISO 3166-1 alpha2 country code

    if (len == 5) {
        // Special case for POSIX name format
        if (str.charAt(2) != '_' || str.charAt(3) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        end = 4; // Adjust substring length for ISO 639-1 language code followed by two underscores and POSIX name
    } else if (len == 7) {
        // Full format
        if (str.charAt(2) != '_' || str.charAt(3) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    // Adjust character range checks for country code and POSIX name
    if ((len == 5 && (!Character.isUpperCase(str.charAt(4)) || !Character.isUpperCase(str.charAt(5)))) ||
        (len == 7 && (!Character.isUpperCase(str.charAt(6))))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Construct Locale object based on length of string
    if (len == 2) {
        return new Locale(str, "");
    } else {
        String countryOrRegion = len == 5 ? "_" : str.substring(4, end); // For 5-character strings, use single underscore as separator
        return new Locale(str.substring(0, 2), countryOrRegion);
    }
}