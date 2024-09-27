public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);
    return contains(collator.compare(str, searchStr) == 0);
}