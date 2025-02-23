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
            pos += c.length; // move position by the number of chars in the glyph (accounts for surrogate pairs)
        } else {
            // Since consumed tells how many code points from 'pos' were translated, move 'pos' by these many
            // code points, considering their actual character count (e.g., handling surrogate pairs especially).
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }
}
