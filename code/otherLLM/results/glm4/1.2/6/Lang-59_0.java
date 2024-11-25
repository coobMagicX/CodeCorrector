public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width); // Ensure capacity for the string plus padding.
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, Math.min(strLen, width), buffer, size);
            size += Math.min(strLen, width); // Update the size with the actual length used.
        } else {
            int padLen = width - strLen; // Calculate padding length.
            str.getChars(0, strLen, buffer, size); // Append original string to the buffer.
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
            size += width; // Update the size with the total length after padding.
        }
    }
    return this;
}