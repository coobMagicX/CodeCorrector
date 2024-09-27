public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);
    return collator.compare(str, searchStr) == 0;
}