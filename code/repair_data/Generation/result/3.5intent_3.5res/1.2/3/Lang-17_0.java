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
        } else {
            int codePoint = Character.codePointAt(input, pos);
            String escaped = StringEscapeUtils.escapeXml(Character.toString(codePoint));
            out.write(escaped);
            pos += Character.charCount(codePoint) - 1;
        }
        pos++;
    }
}