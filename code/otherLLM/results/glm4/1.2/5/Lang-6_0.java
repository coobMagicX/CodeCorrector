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
            pos += c.length; // Fixed: Update position by the length of the characters written
            continue;
        }
        // The loop below is unnecessary since we already incremented `pos` in the previous line.
        // Remove this loop if it's not needed elsewhere or intended for a different purpose.
    }
}