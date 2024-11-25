public int indexOf(String str, int startIndex) {
    // Check for null string or invalid start index
    if (str == null || startIndex < 0 || startIndex >= size) {
        return -1;
    }

    // Convert the search string to a char array for comparison
    char[] searchChars = str.toCharArray();
    int searchLength = searchChars.length;

    // Get the buffer of characters from the StringBuilder instance
    char[] thisBuf = buffer;

    // Ensure we do not go beyond the bounds of the buffer and the search string length
    if (startIndex + searchLength > size) {
        return -1;
    }

    // Loop over the buffer starting at startIndex
    for (int i = startIndex; i <= size - searchLength; i++) {
        boolean match = true;

        // Compare each character in the buffer with the corresponding character in the search string
        for (int j = 0; j < searchLength; j++) {
            if (thisBuf[i + j] != searchChars[j]) {
                match = false;
                break;
            }
        }

        // If all characters matched, return the starting index of the match
        if (match) {
            return i;
        }
    }

    // Return -1 if no match was found
    return -1;
}