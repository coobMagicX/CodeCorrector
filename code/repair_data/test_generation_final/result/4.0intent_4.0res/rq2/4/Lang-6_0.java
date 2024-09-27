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
            int cp = Character.codePointAt(input, pos);
            char[] c = Character.toChars(cp);
            out.write(c);
            pos += Character.charCount(cp);
        } else {
            // Adjusted loop to correctly increment `pos` by the number of code points consumed
            // Ensure `pos` is correctly incremented based on the number of code points, not just characters
            for (int pt = 0; pt < consumed; ) {
                int charCount = Character.charCount(Character.codePointAt(input, pos + pt));
                pt += charCount;
            }
            pos += consumed;
        }
    }
}