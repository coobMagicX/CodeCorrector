public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length());
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                int codePoint = Character.codePointAt(input, pos);
                // Check if the code point is a non-BMP character
                if (codePoint >= 0x10000) {
                    String hexValue = hex(codePoint - 0x10000 << 10 | (codePoint & 0x3FF));
                    out.write('&');
                    out.write('n');
                    out.write('b');
                    out.write('p');
                    out.write(';');
                    out.write(hexValue);
                } else {
                    // Normal character, just write it
                    c = Character.toChars(codePoint);
                    out.write(c);
                }
                pos += Character.charCount(codePoint);
            }
        }
        pos++;
    }
}

public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
}