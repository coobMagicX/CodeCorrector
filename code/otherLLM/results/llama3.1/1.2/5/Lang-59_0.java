public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        
        // If the input string is shorter than or equal to the specified width,
        // just return it without padding.
        if (width <= strLen) {
            str.getChars(0, width, buffer, size);
            size += width;
            return this;
        }
        
        int padLen = width - strLen;
        String strPad = str + repeat(padChar, padLen);
        strPad.getChars(0, strPad.length(), buffer, size);
        size += width;
    }
    return this;
}

// You can also define a utility method to repeat a character a specified number of times:
public static String repeat(char c, int count) {
    StringBuilder sb = new StringBuilder(count);
    for (int i = 0; i < count; i++) {
        sb.append(c);
    }
    return sb.toString();
}