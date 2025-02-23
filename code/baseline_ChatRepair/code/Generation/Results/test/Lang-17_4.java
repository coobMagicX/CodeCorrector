public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length()); 
    while (pos < input.length()) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            int cp = Character.codePointAt(input, pos);
            char[] c = Character.toChars(cp);
            out.write(c);
            pos += Character.charCount(cp); 
        } else {
            
            for (int i = 0; i < consumed; i++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }
}
