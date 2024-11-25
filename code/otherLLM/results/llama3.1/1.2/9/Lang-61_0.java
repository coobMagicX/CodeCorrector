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
    if (strLen > size) {
        return -1;
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length;

    // Fix: Adjusted loop condition to account for the entire string buffer
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j <= strLen && i + j < len; j++) { 
            if (j == strLen) {
                return i;
            }
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
    }
    return -1;
}