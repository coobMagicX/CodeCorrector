public static Locale toLocale(String str) {
    if (str == null || str.isEmpty()) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    int len = str.length();
    if (len != 2 && len != 5) { // Only check for lengths of 2 or 5 as per the intent
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    // Only check character range for language code in length 2
    if (len == 2) {
        return new Locale(str, "");
    } else {
        char ch3 = str.charAt(2); // Region code first character
        char ch4 = str.charAt(3); // Region code second character
        if (ch3 != '_' || ch4 < 'A' || ch4 > 'Z') { // Check for underscore and valid region code
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        return new Locale(str.substring(0, 2), str.substring(3, 5));
    }
}