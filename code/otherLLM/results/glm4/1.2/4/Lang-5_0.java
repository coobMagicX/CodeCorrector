public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    
    // Check for valid length and first two characters
    if (len < 2 || !Character.isLowerCase(str.charAt(0)) || !Character.isLowerCase(str.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Check for underscore after the two letters
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // If length is 5, then there should be a region code after the first underscore
    if (len == 5) {
        final char ch4 = str.charAt(3);
        if (!Character.isUpperCase(str.charAt(3)) || !Character.isUpperCase(str.charAt(4))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    }
    
    // If length is 7 or more, there should be two underscores
    if (len >= 7) {
        final char ch4 = str.charAt(3);
        final char ch5 = str.charAt(4);
        final char ch6 = str.charAt(5);
        
        // Check for second underscore and country code after it
        if (ch6 != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (!Character.isUpperCase(ch4) || !Character.isUpperCase(ch5)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        // Create a Locale object with all three components
        return new Locale(str.substring(0, 2), str.substring(3, 6));
    }

    throw new IllegalArgumentException("Invalid locale format: " + str);
}