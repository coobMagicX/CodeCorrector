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
        // Correctly handle surrogate pairs
        for (int pt = 0; pt < consumed; pt++) {
            int codePoint = Character.codePointAt(input, pos);
            if ((codePoint & 0xFF80) == 0xD800) { 
                out.write('\u005C'); // Write escape character
                out.write((char)((codePoint >> 6) + '\u0040')); // Write high surrogate
                out.write((char)((codePoint & 0x3F) + '\u0040')); // Write low surrogate
            } else {
                pos += Character.charCount(codePoint);
            }
        }
    }
}