public int indexOf(String str, int startIndex) {
    
    if (startIndex < 0) {
        startIndex = 0;
    }
    
    
    if (str == null || startIndex >= size) {
        return -1;
    }
    
    int strLen = str.length();
    
    
    if (strLen == 0) {
        return startIndex;
    }
    
    
    if (strLen > size - startIndex) {
        return -1;
    }
    
    
    int limit = size - strLen + 1;
    
    
    char[] thisBuf = buffer;
    
    outer:
    for (int i = startIndex; i < limit; i++) {
        for (int j = 0; j < strLen; j++) {
            if (thisBuf[i