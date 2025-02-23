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
    int len = size - strLen;   
    for (int i = startIndex; i <= len; i++) {     
        int j;
        for (j = 0; j < strLen; j++) {
            if (thisBuf[i + j] != str.charAt(j)) {
                break;
            }
        }
        if (j == strLen) {
            return i; 
        }
    }
    return -1;  
}
