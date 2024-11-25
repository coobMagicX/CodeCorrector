private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (text == null || text.length() == 0 || searchList == null || 
        searchList.length == 0 || replacementList == null || replacementList.length != searchList.length) {
        return text;
    }

    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }
    
    StringBuilder buf = new StringBuilder();
    int start = 0;

    while (start < text.length()) {
        int index = findNextMatch(text, searchList, start);
        if (index == -1) break;
        
        int end = index + searchList[index].length();
        buf.append(text.substring(start, index));
        buf.append(replacementList[index]);
        start = end;
    }
    buf.append(text.substring(start));

    String result = buf.toString();
    if (!repeat || --timeToLive <= 0) {
        return result;
    } else {
        return replaceEach(result, searchList, replacementList, repeat, timeToLive);
    }
}

private static int findNextMatch(String text, String[] searchList, int start) {
    for (int i = 0; i < searchList.length; ++i) {
        if (!noMoreMatchesForReplIndex[i]) {
            int index = text.indexOf(searchList[i], start);
            if (index != -1 && (i == 0 || index > start)) return index;
        }
    }
    return -1;
}