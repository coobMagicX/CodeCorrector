public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if ((len != 2 && len != 5 && len != 7) || (len > 7)) {
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
        
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else if (len == 7) {
            char ch6 = str.charAt(6);
            if (ch6 < 'A' || ch6 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
        } else {
            throw new IllegalArgumentException("Locale string length must be 2, 5, or 7 characters: " + str);
        }
    }
}