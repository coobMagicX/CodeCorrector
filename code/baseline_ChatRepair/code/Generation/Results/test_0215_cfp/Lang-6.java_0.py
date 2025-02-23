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
            pos += c.length; // Correctly increment by the number of Java chars (code units) related to this code point.
        } else {
            // Move `pos` by the actual number of characters consumed from the input
            pos += consumed;
        }
    }
}
