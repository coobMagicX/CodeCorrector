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
            int cp = Character.codePointAt(input, pos);
            char[] chars = Character.toChars(cp);
            out.write(chars);
            pos += Character.charCount(cp); 
        } else {
            
            for (int i = 0; i < consumed; i++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }
}
