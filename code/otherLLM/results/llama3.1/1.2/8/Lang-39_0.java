private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (isEmpty(text) || isEmpty(searchList) || isEmpty(replacementList)) {
        return text;
    }

    if (timeToLive < 0) {
        throw new IllegalStateException("Time to live is less than 0: " + text);
    }

    int searchLength = searchList.length;
    int replacementLength = replacementList.length;

    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and replace array lengths don't match: " + searchLength + " vs " + replacementLength);
    }

    boolean[] noMoreMatchesForReplacementIndex = new boolean[searchLength];

    int textIndex = -1;
    int replaceIndex = -1;

    for (int i = 0; i < searchLength; i++) {
        if (!noMoreMatchesForReplacementIndex[i] && !isEmpty(searchList[i]) && !isEmpty(replacementList[i])) {
            int tempIndex = text.indexOf(searchList[i]);

            if (tempIndex == -1) {
                noMoreMatchesForReplacementIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
    }

    if (textIndex == -1) {
        return text;
    }

    int start = 0;

    int increase = 0;

    for (int i = 0; i < searchLength; i++) {
        int greater = replacementList[i].length() - searchList[i].length();
        if (greater > 0) {
            increase += 3 * greater;
        }
    }

    increase = Math.min(increase, text.length() / 5);

    StringBuilder buf = new StringBuilder(text.length() + increase);

    while (textIndex != -1) {

        for (int i = start; i < textIndex; i++) {
            buf.append(text.charAt(i));
        }

        buf.append(replacementList[replaceIndex]);

        start = textIndex + searchList[replaceIndex].length();

        textIndex = -1;
        replaceIndex = -1;

        for (int i = 0; i < searchLength; i++) {
            if (!noMoreMatchesForReplacementIndex[i] && !isEmpty(searchList[i]) && !isEmpty(replacementList[i])) {
                int tempIndex = text.indexOf(searchList[i], start);

                if (tempIndex == -1) {
                    noMoreMatchesForReplacementIndex[i] = true;
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
    } else {
        // Consider adding a max depth or recursion limit here
        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }
}

private static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
}