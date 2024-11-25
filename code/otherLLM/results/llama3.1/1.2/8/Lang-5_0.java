public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    
    // Consolidated validation logic
    if (!(len >= 2 && len <= 7)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Standardized error handling
    try {
        return parseLocale(str);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid locale format: " + str, e);
    }
}

private static Locale parseLocale(final String str) {
    if (str.length() == 2) {
        return new Locale(str);
    }
    
    final char ch2 = str.charAt(2);
    if (ch2 != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    final char ch3 = str.charAt(3);
    if (Character.isUpperCase(ch3)) {
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    } else if (ch3 == '_') {
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }
    
    final char ch4 = str.charAt(4);
    if (!Character.isUpperCase(ch4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Improved country code processing
    if (str.length() == 5) {
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    } else if (str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    final char ch6 = str.charAt(6);
    if (!Character.isUpperCase(ch6)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(7));
}