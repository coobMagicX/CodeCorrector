public int indexOf(String str, int startIndex) {
    startIndex = Math.max(startIndex, 0); // Ensure that the start index is not negative
    if (str == null || startIndex >= size) {
        return -1; // Return -1 if str is null or startIndex is out of bounds
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex); // Delegate to single char indexOf if str length is 1
    }
    if (strLen == 0) {
        return startIndex; // Return the start index if str is empty
    }
    if (strLen > size - startIndex) {
        return -1; // Return -1 if the string to search is longer than the substring from startIndex
    }
    char[] thisBuf = buffer; // Assuming 'buffer' is a char array representing the current object's data
    int end = size - strLen; // Adjust end index for search
    outer:
    for (int i = startIndex; i <= end; i++) { // Loop from startIndex to adjusted end
        for (int j = 0; j < strLen; j++) { // Check every character in str
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer; // Continue outer loop if a mismatch is found
            }
        }
        return i; // Return the index if entire string matches
    }
    return -1; // Return -1 if no match is found
}