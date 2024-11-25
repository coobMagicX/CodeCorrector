public int indexOf(String str, int startIndex) {
    if (str == null || str.isEmpty()) {
        return -1;
    }
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (startIndex >= size) {
        return -1;
    }
    int strLen = str.length();
    char[] thisBuf = buffer;
    int len = Math.min(size, startIndex + strLen);

    for (int i = startIndex; i < len; i++) {
        boolean match = true;
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                match = false;
                break;
            }
        }
        if (match) {
            return i;
        }
    }
    return -1;
}