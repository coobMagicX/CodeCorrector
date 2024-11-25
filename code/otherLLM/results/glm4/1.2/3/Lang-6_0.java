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
            // Use Character.codePointAt to ensure surrogate pairs are handled correctly.
            int codepoint = Character.codePointAt(input, pos);
            char[] c = Character.toChars(codepoint);
            out.write(c);
            // Update the position only after writing all characters of the code point.
            pos += c.length;
        } else {
            // The translator has already taken care of a surrogate pair,
            // so we just update the position without additional operations.
            pos += consumed;
        }
    }
}