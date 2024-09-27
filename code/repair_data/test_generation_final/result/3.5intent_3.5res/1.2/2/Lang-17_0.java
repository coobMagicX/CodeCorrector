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
        }
        else {
            for (int pt = 0; pt < consumed; pt++) {
                if (pos < len - 1) {
                    pos += Character.charCount(Character.codePointAt(input, pos));
                } else {
                    pos++;
                }
            }
        }
        pos++;
    }
}