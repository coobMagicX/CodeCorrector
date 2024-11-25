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
            pos += c.length; // Move past the full code point
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                int codePoint = Character.codePointAt(input, pos + pt);
                if (Character.isSurrogatePair(codePoint, input.charAt(pos + pt + 1))) {
                    // Write the surrogate pair as is
                    out.write(codePoint);
                    out.write(Character.charCount(codePoint));
                } else {
                    // Write the single code point
                    char[] cpChars = Character.toChars(codePoint);
                    out.write(cpChars);
                }
                pos += Character.charCount(codePoint); // Move past the current code point
            }
        }
    }
}