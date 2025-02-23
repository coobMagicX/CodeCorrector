public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
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
    }

    if (str.charAt(2) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    int pos = 3; // Position after the first underscore
    String country = "";
    String variant = "";

    // Getting country code
    if (len > 3 && Character.isUpperCase(str.charAt(pos)) && Character.isUpperCase(str.charAt(pos+1))) {
        country = str.substring(pos, pos + 2);
        pos += 2;
    }
    
    // Getting variant
    if (pos < len) {
        if (str.charAt(pos) == '_') {
            pos++; // Skip the underscore before variant
            if (pos < len) {
                variant = str.substring(pos);
            }
        } else {
            // If we are expecting a variant, starting position must be a '_'
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    return new Locale(str.substring(0, 2), country, variant);
}
