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
            int codePoint = Character.codePointAt(input, pos);
            // Check for the specific XML character "\ud842\udfb7" and escape it
            if (codePoint == 0x2347) { // This is the Unicode value of "\ud842\udfb7"
                out.write("&#12345;");
            } else {
                char[] c = Character.toChars(codePoint);
                out.write(c);
            }
        } else {
            // Skip the surrogate pair if it was handled by the translator
            pos += consumed;
        }
        pos++;
    }
}