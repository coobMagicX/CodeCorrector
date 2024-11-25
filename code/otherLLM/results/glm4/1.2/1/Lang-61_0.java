public int indexOf(String str, int startIndex) {
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (str == null || startIndex >= size) {
        return -1;
    }
    int strLen = str.length();
    if (strLen == 0) {
        // If the string to match is empty, we should start from startIndex
        return startIndex;
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen;
    outer:
    for (int i = startIndex; i <= len; i++) { // Changed condition to include equal sign since we need to check till the last possible index
        boolean matchFound = true;
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) { // Check for a non-matching character
                matchFound = false;
                break outer; // Break out of the inner loop and continue to the next position in buffer
            }
        }
        if (matchFound) { // If no mismatch is found, return the current index
            return i;
        }
    }
    return -1; // Return -1 if no match is found
}