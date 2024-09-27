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
            if (Character.isHighSurrogate((char) codePoint)) {
                if (pos + 1 < len && Character.isLowSurrogate(input.charAt(pos + 1))) {
                    out.write(input.charAt(pos));
                    out.write(input.charAt(pos + 1));
                    pos += 2;
                } else {
                    throw new IllegalArgumentException("Invalid surrogate pair at index " + pos);
                }
            } else {
                char[] c = Character.toChars(codePoint);
                out.write(c);
                pos += c.length;
            }
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                int codePoint = Character.codePointAt(input, pos);
                char[] c = Character.toChars(codePoint);
                out.write(c);
                pos += Character.charCount(codePoint);
            }
        }
    }
}