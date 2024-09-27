public int indexOf(String str, int startIndex) {
    if (str == null) {
        return -1;
    }
    startIndex = Math.max(startIndex, 0);
    if (startIndex >= this.size) {
        return -1;
    }
    int strLen = str.length();
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    }
    if (strLen > this.size) {
        return -1;
    }
    char[] thisBuf = this.buffer;
    int len = this.size - strLen + 1;
    outer:
    for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (thisBuf[i + j] != str.charAt(j)) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}