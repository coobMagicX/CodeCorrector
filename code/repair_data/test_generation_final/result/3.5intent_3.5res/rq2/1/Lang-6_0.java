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
            int codePoint = Character.codePointAt(input, pos);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                char[] c = Character.toChars(codePoint);
                out.write(c);
                pos += c.length;
            } else {
                out.write(codePoint);
                pos++;
            }
            continue;
        }
        for (int pt = 0; pt < consumed; pt++) {
            pos++;
        }
    }
}