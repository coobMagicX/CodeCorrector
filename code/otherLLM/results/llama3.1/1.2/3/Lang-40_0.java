public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    // Use substringBetween method to extract the part of the string that should be searched
    String searchStrUpper = str.toUpperCase();
    String strToSearchUpper = substringBetween(searchStrUpper, searchStrUpper.toLowerCase(), searchStrUpper.toLowerCase());
    
    if (strToSearchUpper != null) {
        return searchStrUpper.contains(strToSearchUpper);
    }
    
    // If no match was found using the extracted part, try searching the entire string
    return str.toUpperCase().contains(searchStr.toUpperCase());
}