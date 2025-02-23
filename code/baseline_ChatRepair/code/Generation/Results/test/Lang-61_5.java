public int indexOf(String str, int startIndex) {
    
    startIndex = Math.max(startIndex, 0); 
    if (str == null || startIndex >= size) {
        return -1; 
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex); 
    }
    if (strLen == 0) {
        return startIndex >= size ? -1 : startIndex; 
    }
    if (strLen > size - startIndex) {
        return -1; 
    }
    
    char[] thisBuf = buffer;
    
    int len = size - strLen + 1; 
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (thisBuf[i + j] != str.charAt(j)) {
                continue outer; 
            }
        }
        return i; 
   