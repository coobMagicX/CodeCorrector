private static String replaceEach(String text, String[] searchList, String[] replacementList, 
                                  boolean repeat, int timeToLive) 
{

    // mchyzer Performance note: This creates very few new objects (one major goal)
    // let me know if there are performance requests, we can create a harness to measure

    if (isEmpty(text) || searchList == null || 
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

    StringBuilder buf = new StringBuilder(text.length());

    int textIndex = -1;
    int replaceIndex = -1;

    // index on index that the match was found
    for (int i = 0; i < searchLength; i++) {
        if (isNotBlank(searchList[i]) && isNotBlank(replacementList[i])) {
            int tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                continue;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
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
        int greater = replacementList[i].length() - searchList[i].length();
        if (greater > 0) {
            increase += 3 * greater; // assume 3 matches
        }
    }
    // have upper-bound at 20% increase, then let Java take over
    increase = Math.min(increase, text.length() / 5);

    while (textIndex != -1) {

        for (int i = start; i < textIndex; i++) {
            buf.append(text.charAt(i));
        }
        buf.append(replacementList[replaceIndex]);

        start = textIndex + searchList[replaceIndex].length();

        // find the next earliest match
        textIndex = -1;
        replaceIndex = -1;
        for (int i = 0; i < searchLength; i++) {
            if (isNotBlank(searchList[i]) && isNotBlank(replacementList[i])) {
                int tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    continue;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
        }

    }
    int textLength = text.length();
    for (int i = start; i < textLength; i++) {
        buf.append(text.charAt(i));
    }
    String result = buf.toString();
    if (!repeat) {
        return result;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}