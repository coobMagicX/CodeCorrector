public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        int copyChars = Math.min(strLen, width); 
        str.getChars(0, copyChars, buffer, size);
        
        
        if (strLen < width) {
            int padLen = width - strLen; 
            for (int i = 0; i < padLen; i++) {
                buffer[size + copyChars + i] = padChar;
            }
        }

        size += width;
    }
    return this;
}
