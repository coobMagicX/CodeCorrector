public int indexOf(String str, int startIndex) {
    startIndex = Math.max(startIndex, 0); 
    if (str == null) {
        return -1; 
    }
    int strLen = str.length();
    if (strLen == 0) {
        return startIndex >= size ? -1 : startIndex; 
    }
    if (strLen > size - startIndex) {
        return -1; 
    }
    char[] thisBuf = buffer;
    
    
    int lastPossibleStartIndex = size - strLen;
    outer:
    for (int i = startIndex; i <= lastPossibleStartIndex; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;  
            }
        }
        return i;  
    }
    return