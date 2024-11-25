private static String replaceEach(String text, String[] searchList, String[] replacementList, 
                                  boolean repeat, int timeToLive) 
{
    if (text == null || text.length() == 0 || searchList == null || 
        searchList.length == 0 || replacementList == null || replacementList.length == 0) 
    {
        return text;
    }

    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }

    int searchLength = searchList.length;
    int replacementLength = replacementList.length;

    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and Replace array lengths don't match: "
            + searchLength
            + " vs "
            + replacementLength);
    }

    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

    int textIndex = -1;
    int replaceIndex = -1;
    int tempStartPos = 0;

    while ((textIndex = indexOf(text, searchList[replaceIndex], tempStartPos)) != -1) {
        String replacement = (replacementList[replaceIndex] == null) ? "" : replacementList[replaceIndex];
        text = replace(text, text.substring(0, textIndex), replacement + text.substring(textIndex), 1);
        tempStartPos = textIndex + replacement.length();
    }

    if (!repeat) {
        return text;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}

// Method to check for empty string (not provided in the methods list, so I implemented it)
private static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
}