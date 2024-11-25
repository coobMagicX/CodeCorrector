private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    // Check for null or empty inputs
    if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
        return text;
    }

    // Check the length of searchList and replacementList
    if (searchList.length != replacementList.length) {
        throw new IllegalArgumentException("Search list and replacement list must be of the same length.");
    }

    // If timeToLive is less than 0, do not perform any replacements
    if (timeToLive < 0) {
        return text;
    }

    StringBuilder buf = new StringBuilder();
    int lastEndIndex = 0;

    for (int i = 0; i < searchList.length; i++) {
        String search = searchList[i];
        String replacement = replacementList[i];

        int index = text.indexOf(search, lastEndIndex);

        // If the search string is not found after the current position, break the loop
        if (index == -1) {
            break;
        }

        // Append the portion of text before the search string
        buf.append(text.substring(lastEndIndex, index));

        // Append the replacement string
        buf.append(replacement);

        // Update the lastEndIndex to the end of the current search string
        lastEndIndex = index + search.length();
    }

    // Append any remaining portion of text after the last replaced occurrence
    if (lastEndIndex < text.length()) {
        buf.append(text.substring(lastEndIndex));
    }

    String result = buf.toString();

    // If repeat is true, call replaceEach recursively with reduced timeToLive
    return repeat ? replaceEach(result, searchList, replacementList, repeat, timeToLive - 1) : result;
}