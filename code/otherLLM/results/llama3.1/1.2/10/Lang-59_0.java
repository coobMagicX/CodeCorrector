public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0 && width != 0) { // Check for zero width padding
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, strLen, buffer, size);
        } else {
            int padLen = width - strLen;
            for (int i = 0; i < padLen; i++) { // Correctly calculate the index to store padChar
                buffer[size + strLen + i] = padChar;
            }
            str.getChars(0, strLen, buffer, size); // Move string data before padding
        }
        size += width;
    }
    return this;
}