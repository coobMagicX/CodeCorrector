public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int charIndex = 0; 
    int len = input.length(); 
    while (charIndex < len) {
        int consumed = translate(input, charIndex, out);
        if (consumed == 0) {
            int cp = Character.codePointAt(input, charIndex);
            char[] c = Character.toChars(cp);
            out.write(c);
            charIndex += Character.charCount(cp); 
        } else {
            charIndex += consumed; 
        }
        pos++; 
    }
}
