public char[] expandCurrentSegment() {
    final char[] curr = _currentSegment;
    // Let's grow by 50% by default
    final int len = curr.length;
    
    // check if the current length is equal to the maximum segment length
    if (len == MAX_SEGMENT_LEN) {
        // if so, expand by 25% instead of the default 50%
        int newLen = len + (len >> 2);
        return (_currentSegment = Arrays.copyOf(curr, newLen));
    } else if (len < MAX_SEGMENT_LEN) {
        // if the current length is less than the maximum segment length, expand by 50%
        int newLen = Math.min(MAX_SEGMENT_LEN, len + (len >> 1));
        return (_currentSegment = Arrays.copyOf(curr, newLen));
    } else {
        // if the current length is greater than the maximum segment length, expand to the maximum segment length
        return (_currentSegment = Arrays.copyOf(curr, MAX_SEGMENT_LEN));
    }
}