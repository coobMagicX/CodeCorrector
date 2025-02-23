public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, width, buffer, size); // modified to use width instead of strLen
        } else {
            str.getChars(0, strLen, buffer, size);
            int padLen = width - strLen; // How many padding characters to append
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar;
            }
        }
        size += width; // the actual size increased by width regardless of the length of 'str'
    }
    return this;
}
