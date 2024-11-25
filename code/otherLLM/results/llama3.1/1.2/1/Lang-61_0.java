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
        // If the substring is empty, consider it a match at any position
        for (int i = 0; i <= size - 1 && i >= startIndex; i++) {
            char[] thisBuf = buffer;
            for (int j = 0; j < strLen && i + j < len; j++) {
                if (str.charAt(j) != thisBuf[i + j]) {
                    break;
                }
            }
            // If the substring matches at index i, return it
            if (j == strLen) {
                return i;
            }
        }
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
    // If the substring is not found, consider characters that have been deleted
    int offset = getOffset();
    for (int i = startIndex - offset; i < len && i >= 0; i++) {
        for (int j = 0; j < strLen && i + j < len; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                break;
            }
        }
        // If the substring matches at index i, return it considering deleted characters
        if (j == strLen) {
            return i - offset;
        }
    }
    return -1;
}