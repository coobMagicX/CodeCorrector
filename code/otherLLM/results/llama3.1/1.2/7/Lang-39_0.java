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

    // check for null safety in arrays
    if (searchList == null || replacementList == null) {
        throw new NullPointerException("Search list and/or replacement list cannot be null");
    }

    // verify array length consistency
    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and Replace array lengths don't match: "
            + searchLength
            + " vs "
            + replacementLength);
    }

    for (int i = 0; i < searchList.length; i++) {
        if (searchList[i] == null || replacementList[i] == null) {
            throw new NullPointerException("Individual elements in the search list and/or replacement list cannot be null");
        }
    }

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
    // NOTE: logic duplicated below START
    for (int i = 0; i < searchLength; i++) {
        if (noMoreMatchesForReplIndex[i] || searchList[i].length() == 0 || replacementList[i].length() == 0) 
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
    // NOTE: logic mostly below END

    // no search strings found, we are done
    if (textIndex == -1) {
        return text;
    }

    int start = 0;

    // get a good guess on the size of the result buffer so it doesnt have to double if it goes over a bit
    int increase = 0;

    // count the replacement text elements that are larger than their corresponding text being replaced
    for (int i = 0; i < searchList.length; i++) {
        int greater = replacementList[i].length() - searchList[i].length();
        if (greater > 0) {
            increase += greater;
        }
    }

    // create a StringBuilder to build the result string
    StringBuilder buf = new StringBuilder(text.length() + increase);

    // add text before first match
    int textLength = text.length();
    for (int i = 0; i < start; i++) {
        buf.append(text.charAt(i));
    }

    // loop through matches and replace them
    while (textIndex != -1) {
        if (!repeat && searchList[replaceIndex].length() > 1) {
            break;
        }
        buf.append(replacementList[replaceIndex]);
        start = textIndex + searchList[replaceIndex].length();
        for (int i = 0; i < searchLength; i++) {
            tempIndex = text.indexOf(searchList[i], start);
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
    }

    // add any remaining text
    for (int i = start; i < textLength; i++) {
        buf.append(text.charAt(i));
    }

    String result = buf.toString();
    if (!repeat) {
        return result;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}