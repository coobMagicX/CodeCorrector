public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        
        // Calculate remaining padding length by considering the case where input string is already at or near specified width
        int padLen = Math.max(width - strLen, 0);
        
        if (strLen >= width) {
            str.getChars(0, strLen, buffer, size);
        } else {
            str.getChars(0, strLen, buffer, size);
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
        }
        size += width;
    }
    return this;
}