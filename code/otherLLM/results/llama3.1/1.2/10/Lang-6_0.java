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
            if (c.length == 2 && Character.isSurrogate(c[0])) { // Check for surrogate pairs
                writeSurrogatePair(out, c); // Handle surrogate pair specially
            } else {
                out.write(c);
            }
            pos += c.length;
            continue;
        }
        // contract with translators is that they have to understand codepoints 
        // and they just took care of a surrogate pair
        for (int pt = 0; pt < consumed; pt++) {
            pos += Character.charCount(Character.codePointAt(input, pos));
        }
    }
}

private void writeSurrogatePair(Writer out, char[] c) throws IOException {
    // Special handling for surrogate pairs: write the low and high surrogates as a single codepoint
    int codePoint = (c[0] << 10) + c[1];
    out.write(Character.toChars(codePoint));
}