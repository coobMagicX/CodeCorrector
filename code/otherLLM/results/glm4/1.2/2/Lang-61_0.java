public int indexOf(String str, int startIndex) {
    // Check if the string or substring is null or empty
    if (str == null || str.length() == 0) {
        return -1;
    }

    // Adjust the start index to be within bounds
    startIndex = (startIndex < 0 ? 0 : startIndex);

    // If the start index is beyond the buffer, return -1
    if (startIndex >= size) {
        return -1;
    }

    int strLen = str.length();
    char[] thisBuf = buffer;

    outer:
    for (int i = startIndex; i <= size - strLen; i++) {
        boolean matchFound = true;

        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                matchFound = false;
                break outer;
            }
        }

        if (matchFound) {
            return i;
        }
    }

    return -1;
}