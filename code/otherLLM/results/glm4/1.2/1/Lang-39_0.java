private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (text == null || text.length() == 0 || searchList == null || 
        searchList.length == 0 || replacementList == null || replacementList.length != searchList.length) {
        return text;
    }

    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }

    boolean[] noMoreMatchesForReplIndex = new boolean[searchList.length];
    StringBuilder buf = new StringBuilder();
    int start = 0;

    while (timeToLive > 0) {
        boolean replacedSomething = false;
        for (int i = 0; i < searchList.length; i++) {
            if (!noMoreMatchesForReplIndex[i]) {
                int index = text.indexOf(searchList[i], start);
                if (index >= 0) {
                    buf.append(text, start, index).append(replacementList[i]);
                    start = index + searchList[i].length();
                    noMoreMatchesForReplIndex[i] = true;
                    replacedSomething = true;
                }
            }
        }

        if (!replacedSomething) {
            break;
        }

        text = buf.toString();
        buf.setLength(0); // Clear the StringBuilder for reuse
        timeToLive--;
    }

    return text;
}