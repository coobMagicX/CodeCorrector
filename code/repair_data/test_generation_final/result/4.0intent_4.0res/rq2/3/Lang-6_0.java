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
            char[] c = Character.toChars(codePoint);
            out.write(c);
            pos += Character.charCount(codePoint);
            continue;
        }
        // contract with translators is that they have to understand codepoints 
        // and they just took care of a surrogate pair
        pos += consumed;
    }
}