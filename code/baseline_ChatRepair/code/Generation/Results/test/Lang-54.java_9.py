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
    // Base language
    String language = str.substring(0, 2);

    // Optional parts
    String country = "";
    String variant = "";

    // Process country and variant parts if present
    if (len > 2) {
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        // There is an underscore, need to process further
        int pos = str.indexOf('_', 3);
        if (pos == -1) {
            // No second underscore
            if (len > 5) {
                country = str.substring(3, 5);
                if (len > 5) {
                    variant = str.substring(5);
                }
            } else {
                country = (len > 3) ? str.substring(3) : "";
            }
        } else {
            // Second underscore found
            country = str.substring(3, pos);
            variant = str.substring(pos + 1);
        }
    }

    return new Locale(language, country, variant);
}
