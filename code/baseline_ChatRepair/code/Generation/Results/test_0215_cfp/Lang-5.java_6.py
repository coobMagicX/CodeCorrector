public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    int pos = 0;
    final char ch0 = str.charAt(pos);
    
    // Check if the locale starts with an underscore (country only)
    if (ch0 == '_') {
        if (len < 4) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        String country = str.substring(1, 3);
        if (!Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 3) {
            return new Locale("", country);
        } else if (len > 3 && str.charAt(3) == '_') {
            String variant = str.substring(4);
            return new Locale("", country, variant);
        }
    }
    
    if (!Character.isLowerCase(ch0) || (len > 1 && !Character.isLowerCase(str.charAt(1)))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    if (len == 2) {
        return new Locale(str);
    }
    if (len < 5 || str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    String country = str.substring(3, 5);
    if (!Character.isUpperCase(country.charAt(0)) || !Character.isUpperCase(country.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    if (len == 5) {
        return new Locale(str.substring(0, 2), country);
    }
    
    if (len < 7 || str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    String variant = str.substring(6);
    return new Locale(str.substring(0, 2), country, variant);
}
