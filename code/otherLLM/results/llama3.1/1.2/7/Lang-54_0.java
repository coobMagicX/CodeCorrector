public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    
    // Validate the '_' character position
    boolean hasUnderscore = str.contains("_");
    if (!hasUnderscore && len != 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Update locale creation logic
    String[] parts;
    if (hasUnderscore) {
        parts = str.split("_");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch2 = parts[1].charAt(0);
        char ch3 = parts[1].charAt(1);
        if (ch2 < 'A' || ch2 > 'Z' || ch3 < 'A' || ch3 > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    } else {
        parts = new String[] {str, "", ""};
    }

    // Ensure consistent error handling
    if (hasUnderscore && len != 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    return hasUnderscore ? 
           new Locale(parts[0], parts[1], parts[2]) : 
           new Locale(str, "");
}