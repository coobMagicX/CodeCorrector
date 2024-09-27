public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                // ch is a supplementary character
                // ch is in the Basic Multilingual Plane
                return true;
            }
        }
    }
    return false;
}

public static int indexOfDifference(CharSequence[] css) {
    if (css == null || css.length <= 1) {
        return INDEX_NOT_FOUND;
    }
    boolean anyStringNull = false;
    boolean allStringsNull = true;
    int arrayLen = css.length;
    int shortestStrLen = Integer.MAX_VALUE;
    int longestStrLen = 0;

    // find the min and max string lengths; this avoids checking to make
    // sure we are not exceeding the length of the string each time through
    // the bottom loop.
    for (int i = 0; i < arrayLen; i++) {
        if (css[i] == null) {
            anyStringNull = true;
            shortestStrLen = 0;
        } else {
            allStringsNull = false;
            shortestStrLen = Math.min(css[i].length(), shortestStrLen);
            longestStrLen = Math.max(css[i].length(), longestStrLen);
        }
    }

    // handle lists containing all nulls or all empty strings
    if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
        return INDEX_NOT_FOUND;
    }

    // handle lists containing some nulls or some empty strings
    if (shortestStrLen == 0) {
        return 0;
    }

    // find the position with the first difference across all strings
    int firstDiff = -1;
    for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
        char comparisonChar = css[0].charAt(stringPos);
        for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
            if (css[arrayPos].charAt(stringPos) != comparisonChar) {
                firstDiff = stringPos;
                break;
            }
        }
        if (firstDiff != -1) {
            break;
        }
    }

    if (firstDiff == -1 && shortestStrLen != longestStrLen) {
        // we compared all of the characters up to the length of the
        // shortest string and didn't find a match, but the string lengths
        // vary, so return the length of the shortest string.
        return shortestStrLen;
    }
    return firstDiff;
}