public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, width, buffer, size); // Fixed to copy only the required width
        } else {
            str.getChars(0, strLen, buffer, size);
            for (int i = 0; i < width - strLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
        }
        size += width;
    }
    return this;
}