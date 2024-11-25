public int indexOf(String str, int startIndex) {
    validateIndex(startIndex); // Validate that the startIndex parameter is within valid bounds.
    
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (str == null || startIndex >= size) {
        return -1;
    }
    
    int strLen = str.length();
    if (strLen == 1) {
        for (int i = startIndex; i <= size && thisBuf[i] != str.charAt(0); i++) { // Update search logic to correctly handle the case when the substring is not found.
            continue;
        }
        return i == size ? -1 : i;
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
    
    // Update search logic to correctly handle the case when the substring is not found.
    for (int i = startIndex; i <= size && i < strLen; i++) {
        if (thisBuf[i] != str.charAt(0)) {
            continue outer;
        }
    }
    return thisBuf.length >= strLen ? i : -1;
}