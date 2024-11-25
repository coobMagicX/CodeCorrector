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
//          contract with translators is that they have to understand codepoints 
//          and they just took care of a surrogate pair
            for (int pt = 0; pt < consumed; pt++) {
                int codepoint = Character.codePointAt(input, pos);
                if (Character.isSurrogate(codepoint)) {
                    // If it's a surrogate pair, increment the position by 2
                    pos += 2;
                } else {
                    // Otherwise, increment the position by the char count of the codepoint
                    pos += Character.charCount(codepoint);
                }
            }
        }
        pos++;
    }
}