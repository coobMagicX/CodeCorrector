public int indexOf(String str, int startIndex) {
    
    startIndex = Math.max(startIndex, 0);

    
    if (str == null || startIndex >= size) {
        return -1;
    }

    
    int strLen = str.length();

    
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    } else if (strLen == 0) {
        return startIndex;
    }

    
    if (strLen > (size - startIndex)) {
        return -1;
    }

    
    char[] thisBuf = buffer;

    
    int endLimit = size - strLen;

    
    outer:
    for (int i = startIndex; i <= endLimit; i++) {
        
        for (int j = 0; j < strLen; j++) {
           