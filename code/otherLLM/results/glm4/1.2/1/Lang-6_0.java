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
            // Assuming that the translate method consumes code points,
            // we need to make sure that the position is incremented correctly
            for (int pt = 0; pt < consumed; pt++) {
                int codePoint = Character.codePointAt(input, pos + pt);
                if (Character.isSurrogate(codePoint)) {
                    // Handle surrogate pairs separately
                    out.write(Character.lowSurrogate(codePoint));
                    out.write(Character.highSurrogate(codePoint));
                } else {
                    // Write the code point as is
                    String hexCode = hex(codePoint);
                    for (int i = 0; i < 8 - hexCode.length(); i++) {
                        out.write('0'); // Pad with zeros to make sure it's always 16 bits
                    }
                    out.write(hexCode);
                }
                pos += Character.charCount(codePoint);
            }
        }
    }
}

public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
}