public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, width, buffer, size); 
        } else {
            str.getChars(0, strLen, buffer, size);
            int padStart = size + strLen; 
            for (int i = padStart; i < size + width; i++) {
                buffer[i] = padChar;
            }
        }
        size += width; 
    }
    return this;
}
