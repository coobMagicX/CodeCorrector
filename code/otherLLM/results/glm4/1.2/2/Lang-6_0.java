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
            // Directly write the code point as a UTF-16 sequence if it is not a surrogate pair
            if (!Character.isSurrogatePair(input.charAt(pos), input.charAt(pos + 1))) {
                char[] c = Character.toChars(Character.codePointAt(input, pos));
                out.write(c);
                pos += c.length;
            } else {
                // Handle surrogate pairs correctly by writing the corresponding four-byte sequence
                int codepoint = Character.codePointAt(input, pos);
                String escapedSequence = "\\u" + hex(codepoint >> 12 & 0xF) + "\\u" + hex(codepoint >> 8 & 0xF) + "\\u" + hex(codepoint >> 4 & 0xF) + "\\u" + hex(codepoint & 0xF);
                out.write(escapedSequence);
                pos += Character.charCount(codepoint); // Move past the surrogate pair
            }
        } else {
            // The translator handled a code point, move the position accordingly
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }
}

public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
}