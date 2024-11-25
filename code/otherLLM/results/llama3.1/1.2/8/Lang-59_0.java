public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            // If string length is already greater than or equal to width, padChar should not be added
            // So we directly return the StrBuilder instance without padding
            size += strLen;
            System.arraycopy(str.toCharArray(), 0, buffer, size - strLen, strLen);
        } else {
            int padLen = width - strLen;
            String strPad = str + repeatChar(padChar, padLen);
            ensureCapacity(size + padLen);
            strPad.getChars(0, strPad.length(), buffer, size);
            size += padLen + strLen;
        }
    }
    return this;
}

// Util method to repeat a character
public StrBuilder repeatChar(char c, int times) {
    char[] chars = new char[times];
    for (int i = 0; i < times; i++) {
        chars[i] = c;
    }
    return appendChars(chars);
}