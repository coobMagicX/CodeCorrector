public int indexOf(String str, int startIndex) {
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (str == null || startIndex >= this.length()) {
        return -1;
    }
    int strLen = str.length();
    if (strLen == 1) {
        for (int i = startIndex; i <= startIndex + 1 && i < this.length(); i++) {
            if (this.charAt(i) == str.charAt(0)) {
                return i;
            }
        }
        return -1;
    }
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen > this.length() - startIndex) {
        return -1;
    }
    char[] thisBuf = buffer;
    int len = startIndex + strLen;
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}