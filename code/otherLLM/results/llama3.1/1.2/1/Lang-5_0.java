public static Locale toLocale(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch0 = str.charAt(0);
    final char ch1 = str.charAt(1);
    // Clarify underscore handling
    if (ch0 == '_' && len > 2 && Character.isLowerCase(str.charAt(2)) && Character.isUpperCase(str.charAt(3))) {
        return new Locale(str.substring(0, 2), "", str.substring(4));
    }
    // Consistent case checking
    if (!Character.isLowerCase(ch0) || !Character.isLowerCase(ch1)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    }
    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    final char ch3 = str.charAt(3);
    // Fix length checks
    if (len < 5 || (ch3 == '_' && len > 4)) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!Character.isUpperCase(ch3) || !Character.isUpperCase(str.charAt(4))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    // Refine locale parsing
    String country = "";
    String variant = "";
    if (len > 5 && str.charAt(5) == '_') {
        variant = str.substring(6);
    } else {
        country = str.substring(len - 2);
        variant = str.substring(len - 4, len - 2);
    }
    return new Locale(str.substring(0, 2), country, variant);
}