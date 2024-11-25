public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0 && !Character.isISOControl(padChar)) { // Input validation: Check for valid pad character
        ensureCapacity(size + width); // Ensure buffer capacity for padded string
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, strLen, buffer, size);
        } else {
            int padLen = width - strLen;
            for (int i = 0; i < padLen; i++) { // Pad to the right
                buffer[size + strLen + i] = padChar;
            }
            str.getChars(0, strLen, buffer, size);
        }
        size += width;
    }
    return this;
}