private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    // Check for valid inputs
    if (text == null || text.isEmpty() || searchList == null || searchList.length != replacementList.length) {
        throw new IllegalArgumentException("Invalid input");
    }

    // If recursion is not allowed or no more replacements are needed, return the text as is
    if (!repeat || timeToLive <= 0) {
        return text;
    }

    String result = text;
    for (int i = 0; i < searchList.length; i++) {
        String search = searchList[i];
        String replace = replacementList[i];

        // Use a while loop to handle multiple occurrences of the same substring
        int fromIndex = 0;
        int index = result.indexOf(search, fromIndex);
        while (index >= 0) {
            result = result.substring(0, index) + replace + result.substring(index + search.length());
            fromIndex = index + replace.length();
            index = result.indexOf(search, fromIndex);
        }
    }

    // Recursively call the method with reduced timeToLive
    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}