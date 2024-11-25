public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }

    // Use the Collator class to handle locale-specific behavior for international characters.
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY); // This will ignore case and accents but consider other differences

    String upperStr = str.toUpperCase(collator);
    String upperSearchStr = searchStr.toUpperCase(collator);

    return contains(upperStr, upperSearchStr);
}