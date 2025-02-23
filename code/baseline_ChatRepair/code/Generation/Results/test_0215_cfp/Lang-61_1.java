public int indexOf(String str, int startIndex) {
    startIndex = Math.max(0, startIndex); 
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
    if (strLen > size - startIndex) { 
        return -1;
    }
    char[] thisBuf = buffer;
    int endLimit = size - strLen; 
    outer:
    for (int i = startIndex; i <= endLimit; i++) { 
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i; 
    }
    return -1;
}
