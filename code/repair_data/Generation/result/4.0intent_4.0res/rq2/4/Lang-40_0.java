public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    java.text.Collator collator = java.text.Collator.getInstance(java.util.Locale.ENGLISH);
    collator.setStrength(java.text.Collator.PRIMARY); // This setting makes the comparison case-insensitive
    return collator.compare(str, searchStr) == 0;
}