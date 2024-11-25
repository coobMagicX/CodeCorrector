private static String replaceEach(String text, String[] searchList, String[] replacementList,
                                  boolean repeat, int timeToLive) 
{
    // mchyzer Performance note: This creates very few new objects (one major goal)
    // let me know if there are performance requests, we can create a harness to measure

    if (text == null || text.length() == 0 || searchList == null || 
        searchList.length == 0 || replacementList == null || replacementList.length == 0) 
    {
        return text;
    }

    // if recursing, this shouldnt be less than 0
    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }

    int searchLength = searchList.length;
    int replacementLength = replacementList.length;

    // make sure lengths are ok, these need to be equal
    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and Replace array lengths don't match: "
            + searchLength
            + " vs "
            + replacementLength);
    }

    // keep track of which still have matches
    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

    // index on index that the match was found
    int textIndex = -1;
    int replaceIndex = -1;
    int tempIndex = -1;

    // index of replace array that will replace the search string found
    for (int i = 0; i < searchLength; i++) {
        if (noMoreMatchesForReplIndex[i] || searchList[i] == null || 
            searchList[i].length() == 0 || replacementList[i] == null) 
        {
            continue;
        }
        tempIndex = text.indexOf(searchList[i]);

        // see if we need to keep searching for this
        if (tempIndex == -1) {
            noMoreMatchesForReplIndex[i] = true;
        } else {
            if (textIndex == -1 || tempIndex < textIndex) {
                textIndex = tempIndex;
                replaceIndex = i;
            }
        }
    }

    // no search strings found, we are done
    if (textIndex == -1) {
        return text;
    }

    int start = 0;

    // get a good guess on the size of the result buffer so it doesnt have to double if it goes over a bit
    int increase = 0;

    // count the replacement text elements that are larger than their corresponding text being replaced
    for (int i = 0; i < searchList.length; i++) {
        int lenDiff = substring(replacementList[i], 0, replacementList[i].length()).length() -
                     substring(searchList[i], 0, searchList[i].length()).length();
        increase += lenDiff;
    }

    StringBuilder buf = new StringBuilder(text.length() + increase);
    while (textIndex != -1) {
        int end = indexOf(text, searchList[replaceIndex], start);
        if (end == -1) {
            break;
        }
        buf.append(text.substring(start, end));
        buf.append(replacementList[replaceIndex]);
        start = end + substring(searchList[replaceIndex], 0, searchList[replaceIndex].length()).length();
        textIndex = indexOf(text, searchList[replaceIndex], start);
        replaceIndex = (replaceIndex + 1) % searchLength;
    }

    // Append any remaining characters
    buf.append(text.substring(start));

    String result = buf.toString();
    if (!repeat) {
        return result;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}