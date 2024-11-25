public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + Math.min(width, buffer.length));
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, Math.min(strLen, width), buffer, size);
            size += Math.min(strLen, width); // Adjust the size to reflect the actual number of characters written
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
            size += width; // Increase the size by the total width, including padding
        }
    }
    return this;
}