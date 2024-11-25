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
            // The character is not a surrogate pair, write it as is.
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
            pos += c.length;
        } else {
            // A surrogate pair was consumed by the translator. Move past both characters.
            int codePoint1 = Character.codePointAt(input, pos);
            int codePoint2 = Character.codePointAt(input, pos + Character.charCount(codePoint1));
            out.write(Character.toChars(codePoint1));
            out.write(Character.toChars(codePoint2));
            pos += consumed; // 'consumed' is the number of code points that were processed.
        }
    }
}