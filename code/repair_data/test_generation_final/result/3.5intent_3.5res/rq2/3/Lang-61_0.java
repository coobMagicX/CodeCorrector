public int indexOf(String str, int startIndex) {
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
    if (strLen > thisBuf.length) { // Updated condition to check against 'thisBuf.length'
        return -1;
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length; // Updated loop limit to 'thisBuf.length'
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}