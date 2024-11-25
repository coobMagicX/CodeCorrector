public int indexOf(String str, int startIndex) {
    // Ensure the input string and buffer are not null.
    if (str == null || this.buffer == null) {
        return -1;
    }

    // Normalize startIndex.
    startIndex = Math.max(0, startIndex);

    // Get the length of the input string.
    int strLen = str.length();

    // If the input string is empty, there's nothing to match.
    if (strLen == 0) {
        return startIndex;
    }

    // Calculate the maximum index we can start searching from.
    int len = this.buffer.length - strLen;

    // Check if the search range is valid.
    if (startIndex > len) {
        return -1;
    }

    char[] buf = this.buffer;
    outer:
    for (int i = startIndex; i <= len - strLen + 1; i++) { // Changed condition to prevent off-by-one error
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != buf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}