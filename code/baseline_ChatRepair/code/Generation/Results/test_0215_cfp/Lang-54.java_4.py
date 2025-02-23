public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len < 2) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 2) {
        return new Locale(str, "");
    } else if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    } else {
        if (len == 3) {
            return new Locale(str.substring(0, 2), "", "");
        }
        char ch3 = str.charAt(3);
        char ch4 = len >= 5 ? str.charAt(4) : 0;

        if (len == 5 && ch3 >= 'A' && ch3 <= 'Z' && ch4 >= 'A' && ch4 <= 'Z') {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else {
            if (ch3 == '_') {
                // The cases with variant but empty country
                if (len >= 6) {
                    return new Locale(str.substring(0, 2), "", str.substring(5));
                } else {
                    return new Locale(str.substring(0, 2), "", "");
                }
            } else if (len >= 5 && ch3 >= 'A' && ch3 <= 'Z' && ch4 >= 'A' && ch4 <= 'Z') {
                // Normal locale with country code
                if (len == 5) {
                    return new Locale(str.substring(0, 2), str.substring(3, 5));
                } else if (str.charAt(5) == '_') {
                    // Locale with country code and variant
                    return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
                }
            }
        }
    }
    throw new IllegalArgumentException("Invalid locale format: " + str);
}
