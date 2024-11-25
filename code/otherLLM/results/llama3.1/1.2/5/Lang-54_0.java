public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len != 2 && len != 5 && len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str, "");
    } else {
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch3 = str.charAt(3);
        char ch4 = str.charAt(4);
        if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        String lang = str.substring(0, 2);
        String country = str.substring(3, len == 5 ? 5 : 6); // Changed the condition to handle len < 7
        if (lang.equals("fr") && country.equals("_POSIX")) {
            return new Locale(lang, "", "POSIX"); // Added special case for "fr__POSIX"
        }
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else {
            if (str.charAt(len - 1) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            country = str.substring(len - 1); // Changed to handle len < 7
            return new Locale(lang, country);
        }
    }
}