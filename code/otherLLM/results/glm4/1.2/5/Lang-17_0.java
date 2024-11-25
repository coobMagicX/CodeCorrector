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
            pos += Character.charCount(Character.codePointAt(input, pos)); // Correctly move past the code point
        } else {
            // The translator has handled a code point or a surrogate pair. We don't need to do anything here as the translator is expected to handle it correctly.
            // Update position for next iteration after the translator's call.
            pos += consumed;
        }
    }
}