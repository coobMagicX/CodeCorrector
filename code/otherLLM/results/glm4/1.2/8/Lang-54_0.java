public static Locale toLocale(String str) {
    if (str == null || !isValidLocaleFormat(str)) {
        return null;
    }

    int len = str.length();
    if (len != 2 && len != 5 && len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    if (len == 2) {
        // First check for valid language and country codes
        if (!isValidLanguageCode(str.substring(0, 2)) || !isValidCountryCode(str.substring(2))) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), "");
    } else if (len == 5) {
        // Check for valid language code and script
        String lang = str.substring(0, 2);
        String script = str.substring(3, 5);

        if (!isValidLanguageCode(lang) || !isValidScript(script)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(lang, "", script);
    } else {
        // Check for valid language code, script and region
        String lang = str.substring(0, 2);
        String script = str.substring(3, 5);
        String region = str.substring(6);

        if (!isValidLanguageCode(lang) || !isValidScript(script) || !isValidRegion(region)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(lang, "", script, region);
    }
}

private static boolean isValidLocaleFormat(String str) {
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if ((i == 1 || i == 3 || i == 5) && c != '_') { // Check underscores
            return false;
        } else if (i == 0 || i == 2 || i == 4 || i >= 6) { // Check valid character ranges for language, script, and region
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                return false;
            }
        }
    }
    return true;
}

private static boolean isValidLanguageCode(String code) {
    // Simplified check: language code should not be longer than 8 characters and not contain invalid characters
    return code.length() <= 8 && !Character.isDigit(code.charAt(0));
}

private static boolean isValidScript(String script) {
    // Simplified check: script code should not be longer than 8 characters and not contain invalid characters
    return script.length() <= 8 && !Character.isDigit(script.charAt(0));
}

private static boolean isValidRegion(String region) {
    // Simplified check: region code should not be longer than 8 characters and not contain invalid characters
    return region.length() <= 8 && !Character.isDigit(region.charAt(0));
}