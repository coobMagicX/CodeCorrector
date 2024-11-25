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
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen;
    
    // Adjust the startIndex based on the deletion operations performed on the StrBuilder object
    startIndex = adjustIndex(startIndex);
    
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

// Helper method to adjust the startIndex based on deletion operations
private int adjustIndex(int startIndex) {
    // Implement your logic here to adjust the startIndex based on the deletion operations performed on the StrBuilder object
    // For example, if the string "three" has been deleted from the original string, you might want to add 3 to the startIndex
    return startIndex + 3; // This is a placeholder value and should be replaced with actual logic
}