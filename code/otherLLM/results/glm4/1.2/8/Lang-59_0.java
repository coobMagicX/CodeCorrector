public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            // Only append the first character to avoid padding when the string is longer than or equal to the width
            buffer[size++] = str.charAt(0);
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            size += strLen; // Update size after appending non-padded characters
            for (int i = 0; i < padLen; i++) {
                buffer[size++] = padChar;
            }
        }
    }
    return this;
}