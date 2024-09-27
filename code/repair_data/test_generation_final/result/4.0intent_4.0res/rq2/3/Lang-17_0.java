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
        int cp = Character.codePointAt(input, pos);
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(cp);
            out.write(c);
            pos += Character.charCount(cp);
        } else {
            // Here we ensure that the position increment correctly handles supplementary characters
            pos += consumed;
        }
    }
}