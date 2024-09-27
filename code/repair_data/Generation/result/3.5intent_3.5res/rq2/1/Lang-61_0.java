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
    StrMatcher matcher = new StrMatcher(str);
    return indexOf(matcher, startIndex);
}

public int indexOf(StrMatcher matcher, int startIndex) {
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (matcher == null || startIndex >= size) {
        return -1;
    }
    int len = size;
    char[] buf = buffer;
    for (int i = startIndex; i < len; i++) {
        if (matcher.isMatch(buf, i, startIndex, len) > 0) {
            return i;
        }
    }
    return -1;
}