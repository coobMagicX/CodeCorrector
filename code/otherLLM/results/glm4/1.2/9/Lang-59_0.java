public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + Math.min(width, buffer.length)); // Ensure capacity for the padding without exceeding the buffer size.
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        int finalSize = Math.min(strLen, width); // Determine the final size after truncation or padding.

        if (strLen >= width) {
            // If the input string is longer than or equal to the specified width, truncate it.
            str.getChars(0, width, buffer, size);
            size += width;
        } else {
            // If the input string is shorter than the specified width, pad it and then append.
            int padLen = width - strLen; // Calculate padding length.
            System.arraycopy(str.toCharArray(), 0, buffer, size, strLen); // Append the string as is.
            for (int i = 0; i < padLen; i++) {
                buffer[size + strLen + i] = padChar; // Add padding characters.
            }
            size += finalSize;
        }
    }
    return this;
}