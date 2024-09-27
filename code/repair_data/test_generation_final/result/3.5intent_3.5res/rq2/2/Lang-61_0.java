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
    
    StrMatcher matcher = new StrMatcher() {
        @Override
        public int isMatch(char[] buffer, int startIndex, int endIndex, int size) {
            if (endIndex - startIndex < strLen) {
                return 0;
            }
            for (int i = 0; i < strLen; i++) {
                if (buffer[startIndex + i] != str.charAt(i)) {
                    return 0;
                }
            }
            return strLen;
        }
    };
    
    return indexOf(matcher, startIndex);
}