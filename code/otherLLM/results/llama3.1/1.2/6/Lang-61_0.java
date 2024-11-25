public int indexOf(String str, int startIndex) {
    validateIndex(startIndex); // Validate the start index
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (str == null || startIndex >= size) {
        return -1;
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    }
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen > size) {
        return -1;
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen;
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    // If the loop completes without finding a match, check if the substring has been partially deleted
    for (int i = startIndex - 1; i >= 0; i--) { 
        int matchedLen = 0;
        for (int j = 0; j < strLen && i + j < size; j++) {
            if (str.charAt(j) == thisBuf[i + j]) {
                matchedLen++;
            } else {
                break;
            }
        }
        if (matchedLen > 0) { // If a match is found, return the index
            return i - matchedLen;
        }
    }
    return -1; // If no match is found, return -1
}