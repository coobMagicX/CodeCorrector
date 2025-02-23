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
    if (strLen > size - startIndex) {
        return -1;  
    }

    char[] thisBuf = buffer;
    
    int limit = size - strLen + 1;

    
    outer:
    for (int i = startIndex; i < limit; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;  
            }
       