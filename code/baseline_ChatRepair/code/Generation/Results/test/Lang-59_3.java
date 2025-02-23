public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width); 
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        
        if (strLen > width) {
            str = str.substring(0, width); 
            strLen = width; 
        }
        
        str.getChars(0, strLen, buffer, size); 
        
        int paddingNeeded = width - strLen; 
        for (int i = 0; i < paddingNeeded; i++) {
            buffer[size + strLen + i] = padChar; 
        }
        
        size += width