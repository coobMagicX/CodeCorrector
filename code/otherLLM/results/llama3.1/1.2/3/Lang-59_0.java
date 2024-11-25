public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0 && obj != null) { // Added a check for obj being non-null
        ensureCapacity(size + width);
        String str = obj.toString();
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, strLen, buffer, size); // Corrected the indexing in getChars method call
        } else {
            int padLen = width - strLen;
            for (int i = 0; i < padLen; i++) { // Moved the loop to append the padding characters correctly
                buffer[size + strLen + i] = padChar;
            }
            str.getChars(0, strLen, buffer, size);
        }
        size += width;
    } else if (width == 0) { // Added a special case for zero width
        return this; // Return the current StrBuilder instance when width is 0
    } else {
        throw new IllegalArgumentException("Width must be greater than 0"); // Throw an exception for invalid width
    }
    return this;
}