public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len != 2 && len != 5 && (len < 7 || str.charAt(2) != '_' || str.charAt(5) != '_')) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str);
    } else {
        String[] parts = str.split("_", -1);
        String language = parts[0];
        String country = (parts.length > 1) ? parts[1] : "";
        String variant = (parts.length > 2) ? parts[2] : "";

        if ((len == 5 && country.length() != 2) || (len > 5 && (country.length() != 2 || variant.isEmpty()))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(language, country, variant);
    }
}
