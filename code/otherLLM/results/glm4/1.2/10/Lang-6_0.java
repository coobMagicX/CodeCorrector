public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = input.length();
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
            pos += c.length;
        } else {
            // The translator has handled a codepoint or surrogate pair
            for (int pt = 0; pt < consumed; pt++) {
                // Move the position by the number of code units that have been processed
                int codePoint = Character.codePointAt(input, pos + pt);
                if (Character.isSurrogatePair(codePoint)) {
                    out.write((char) ((codePoint - 0xD800) + 0xDC00)); // Write the high surrogate
                    out.write((char) ((codePoint - 0xDC00) + 0xE000)); // Write the low surrogate
                } else {
                    int charCount = Character.charCount(codePoint);
                    if (Character.isHighSurrogate(charCount == 2 ? c[charCount - 1] : codePoint)) {
                        // If the next character is a high surrogate, write it as such
                        out.write((char) codePoint);
                    } else {
                        // Otherwise, write the codepoint directly or use hex representation if needed
                        String hex = hex(codePoint);
                        out.write(hex, 0, hex.length());
                    }
                }
            }
        }
    }
}

// Helper method to convert an integer into a hexadecimal string
public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
}