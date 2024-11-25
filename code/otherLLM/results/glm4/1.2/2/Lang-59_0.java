public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width); // Ensure the buffer has enough capacity to add padding

        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, Math.min(strLen, width), buffer, size); // Only copy up to the required width
        } else {
            int padLen = width - strLen;
            System.arraycopy(str.toCharArray(), 0, buffer, size, strLen); // Copy the string characters to buffer
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar; // Pad with the specified character
            }
        }
        size += width; // Increase the size attribute of StrBuilder to reflect the new buffer size
    }
    return this;
}