public int indexOf(String str, int startIndex) {
    // Ensure the input string and the start index are valid
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (str == null || startIndex >= size) {
        return -1;
    }
    
    int strLen = str.length();
    if (strLen == 0) { // If the substring is empty, there's nothing to find
        return startIndex;
    }
    
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen + 1; // Adjusted length to prevent index out of bounds
    
    for (int i = startIndex; i < len; i++) {
        boolean match = true;
        for (int j = 0; j < strLen; j++) { // Check each character
            if (str.charAt(j) != thisBuf[i + j]) { // If a mismatch is found, break the loop
                match = false;
                break;
            }
        }
        
        if (match) { // If all characters matched, return the current index
            return i;
        }
    }
    
    return -1; // Return -1 if no match was found
}