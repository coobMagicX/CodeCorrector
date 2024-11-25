public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len < 2 || len > 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (!isValidCountryCode(ch0, ch1)) {
        throw new IllegalArgumentException("Invalid country code: " + str.substring(0, 2));
    }
    if (len == 2) {
        return new Locale(str, "");
    } else {
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch3 = str.charAt(3);
        char ch4 = str.charAt(4);
        if (!isValidCountryCode(ch3, ch4)) {
            throw new IllegalArgumentException("Invalid country code: " + str.substring(0, 2));
        }
        String variant = null;
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else {
            if (str.charAt(5) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            variant = str.substring(6);
            if (!variant.equals("POSIX")) {
                throw new IllegalArgumentException("Invalid locale variant: " + str);
            }
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        }
    }
}

private static boolean isValidCountryCode(char ch1, char ch2) {
    for (String country : availableLocaleList()) {
        if (country.startsWith(ch1 + "" + ch2)) {
            return true;
        }
    }
    return false;
}