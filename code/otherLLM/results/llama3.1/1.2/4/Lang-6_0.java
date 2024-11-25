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
            pos+= c.length;
            continue;
        }
        // correctly handle surrogate pairs
        for (int pt = 0; pt < consumed; pt++) {
            int codepoint = Character.codePointAt(input, pos + pt);
            if (Character.isSurrogate(codepoint)) {
                // recurse on the next code point in the surrogate pair
                int nextCodepoint = Character.codePointAt(input, pos + pt + 1);
                for (int i = 0; i < consumed; i++) {
                    out.write(Character.toChars((codepoint << 16) | nextCodepoint));
                    pos += Character.charCount(codepoint);
                    codepoint = nextCodepoint;
                    nextCodepoint = Character.codePointAt(input, pos + pt + 1);
                }
            } else {
                // recurse on the consumed code points
                for (int i = 0; i < consumed; i++) {
                    out.write(Character.toChars(codepoint));
                    pos += Character.charCount(codepoint);
                }
            }
        }
    }
}