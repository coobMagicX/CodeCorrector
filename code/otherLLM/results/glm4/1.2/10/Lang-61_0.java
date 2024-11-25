public int indexOf(String str, int startIndex) {
    // Check for null string or invalid start index
    if (str == null || startIndex < 0) {
        return -1;
    }
    
    // Get the buffer size and string length to be matched
    int strLen = str.length();
    char[] thisBuf = buffer;

    // If the remaining buffer is less than the string length, no match can exist
    if (startIndex + strLen > size) {
        return -1;
    }

    // Loop through the buffer starting at startIndex
    for (int i = startIndex; i < size - strLen + 1; i++) {
        boolean matchFound = true;
        
        // Check each character of the string against the buffer
        for (int j = 0; j < strLen; j++) {
            if (thisBuf[i + j] != str.charAt(j)) {
                matchFound = false;
                break;
            }
        }

        // If a match is found, return the start index of the match
        if (matchFound) {
            return i;
        }
    }
    
    // Return -1 if no match is found
    return -1;
}