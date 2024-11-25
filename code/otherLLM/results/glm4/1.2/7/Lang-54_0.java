public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    // Check for valid length for POSIX format which is at least 7 characters long, including the last part as variant.
    if (len < 7 || (len > 7 && str.charAt(6) != '_')) {
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
        // When len is 5, it's not a valid POSIX format for Locale.
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else {
            if (str.charAt(5) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            // Correctly parse the variant part.
            String variant = str.substring(6);
            return new Locale(str.substring(0, 2), str.substring(3, 5), variant);
        }
    }
}