public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len < 2 || (len != 2 && len != 5 && len < 7)) {
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
        if (len == 3) {
            // Handle language_ (language followed by underscore only)
            return new Locale(str.substring(0, 2), "");
        } else if (len > 6 && str.charAt(3) == '_' && str.charAt(4) == '_') {
            // Handle language__variant case
            return new Locale(str.substring(0, 2), "", str.substring(5));
        } else {
            char ch3 = str.charAt(3);
            char ch4 = str.charAt(4);
            if (len == 5 && (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z')) {
                // Handle case when only language and country are specified without variant
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }
}