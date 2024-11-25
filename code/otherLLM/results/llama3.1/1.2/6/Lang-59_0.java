public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        
        // Corrected the logic to handle cases where input string's length is greater than or equal to specified width
        if (strLen > width) {
            str.getChars(0, width, buffer, size);
            for (int i = 0; i < strLen - width; i++) {
                buffer[size + width + i] = padChar;
            }
            size += width;
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
            size += width;
        }
    }
    return this;
}