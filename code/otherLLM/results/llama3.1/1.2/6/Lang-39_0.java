private static String replaceEach(String text, String[] searchList, String[] replacementList, 
                                  boolean repeat, int timeToLive) 
{

    // mchyzer Performance note: This creates very few new objects (one major goal)
    // let me know if there are performance requests, we can create a harness to measure

    if (isBlank(text) || isBlank(searchList) || isBlank(replacementList)) {
        return text;
    }

    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive);
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

    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

    // index on index that the match was found
    int textIndex = -1;
    int replaceIndex = -1;
    int tempIndex = -1;

    for (int i = 0; i < searchList.length; i++) {
        if (noMoreMatchesForReplIndex[i] || isBlank(searchList[i]) || isBlank(replacementList[i])) {
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

    int increase = 0;

    for (int i = 0; i < searchList.length; i++) {
        int greater = replacementList[i].length() - searchList[i].length();
        if (greater > 0) {
            increase += 3 * greater;
        }
    }
    increase = Math.min(increase, text.length() / 5);

    StringBuilder buf = new StringBuilder(text.length() + increase);

    while (textIndex != -1 && timeToLive >= 0) {

        for (int i = start; i < textIndex; i++) {
            buf.append(text.charAt(i));
        }
        buf.append(replacementList[replaceIndex]);

        start = textIndex + searchList[replaceIndex].length();

        textIndex = -1;
        replaceIndex = -1;
        tempIndex = -1;

        for (int i = 0; i < searchList.length; i++) {
            if (noMoreMatchesForReplIndex[i] || isBlank(searchList[i]) || isBlank(replacementList[i])) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i], start);

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

    }
    int textLength = text.length();
    for (int i = start; i < textLength; i++) {
        buf.append(text.charAt(i));
    }
    String result = buf.toString();
    if (!repeat || timeToLive < 0) {
        return result;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}