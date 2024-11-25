public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            // If the string length is already greater than or equal to the desired width, 
            // we can directly update the buffer and return.
            ensureCapacity(size + strLen);
            str.getChars(0, strLen, buffer, size);
            size += strLen;
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            // We need to calculate the correct offset for padding based on the existing string length.
            ensureCapacity(size + padLen);
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
            size += width;
        }
    }
    return this;
}