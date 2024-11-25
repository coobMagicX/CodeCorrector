public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if ((len != 2 && len != 5 && len != 7) || !Character.isLowerCase(str.charAt(0)) || !Character.isLowerCase(str.charAt(1))) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    String languageCode = str.substring(0, 2);
    if (len == 2) {
        return new Locale(languageCode, "");
    } else {
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        String countryCode = str.substring(3, 5);
        if (len == 5) {
            return new Locale(languageCode, countryCode);
        } else {
            if (str.charAt(5) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            String variantCode = str.substring(6);
            return new Locale(languageCode, countryCode, variantCode);
        }
    }
}