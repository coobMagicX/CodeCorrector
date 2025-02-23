public static boolean isAvailableLocale(Locale locale) {
    if (locale == null) {
        return false;
    }
    if (cAvailableLocaleSet == null) {
        return false;
   