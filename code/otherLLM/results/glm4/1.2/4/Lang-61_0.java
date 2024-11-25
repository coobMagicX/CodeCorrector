public int indexOf(String str, int startIndex) {
    // First, we need a StrMatcher instance for the string we're looking for.
    // Since StrMatcher isn't part of the public API, we will create our own
    // matcher that checks if the substring is present at the given start index.
    StrMatcher matcher = new StrMatcher() {
        @Override
        public int isMatch(char[] buf, int offset, int start, int end) {
            for (int i = 0; i < str.length(); i++) {
                if (buf[offset + i] != str.charAt(i)) {
                    return -1;
                }
            }
            return str.length();
        }
    };

    // Use the indexOf method that takes a StrMatcher to find the index.
    return indexOf(matcher, startIndex);
}