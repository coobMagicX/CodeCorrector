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
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
            pos+= c.length;
            continue;
        }
        // contract with translators is that they have to understand codepoints 
        // and they just took care of a surrogate pair
        if (pos + 1 < len && Character.isSurrogatePair(input.charAt(pos), input.charAt(pos + 1))) {
            pos += 2; // increment the position by 2 as a surrogate pair is a single character
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
                if (pos >= len) {
                    break;
                }
            }
        }
        if (pos >= len) {
            break;
        }
    }
}