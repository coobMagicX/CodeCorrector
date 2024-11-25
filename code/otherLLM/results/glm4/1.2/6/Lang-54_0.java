public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    // Adjusting the length check for "language_country_variant" format
    if ((len != 7 && len != 10) || str.indexOf('_') != 2 || str.indexOf('_', 3) != 6) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch3 = str.charAt(3);
    char ch4 = str.charAt(5);
    if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    // Adjusted substring creation to fit the expected format
    String language = str.substring(0, 2);
    String country = str.substring(3, 5);
    String variant = len == 7 ? str.substring(6) : "";
    
    return new Locale(language, country, variant);
}