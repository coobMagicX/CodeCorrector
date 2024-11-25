public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width); // Ensures there's enough space in the buffer to write the padded string.
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, Math.min(strLen, width), buffer, size); // Only get up to 'width' characters.
        } else {
            int padLen = width - strLen;
            for (int i = 0; i < strLen; i++) { // Write the actual string first.
                buffer[size++] = str.charAt(i);
            }
            for (int i = 0; i < padLen; i++) { // Now write the padding characters on the right.
                buffer[size++] = padChar;
            }
        }
    }
    return this;
}