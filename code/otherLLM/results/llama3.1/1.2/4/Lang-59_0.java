public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + Math.max(width, size + 1)); // Ensure capacity is sufficient for both string and padding
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, strLen, buffer, size);
            size += Math.max(width - strLen, 0); // Only increment size by actual padding length
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
            size += Math.max(width - strLen, 0); // Only increment size by actual padding length
        }
    }
    return this;
}