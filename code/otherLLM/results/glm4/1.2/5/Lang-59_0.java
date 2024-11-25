public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, Math.min(strLen, width), buffer, size);
            size += Math.min(strLen, width);
        } else {
            str.getChars(0, strLen, buffer, size);
            for (int i = strLen; i < width; i++) {
                buffer[size + i] = padChar;
            }
            size = size + strLen + Math.max(0, width - strLen);
        }
    }
    return this;
}