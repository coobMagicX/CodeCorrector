public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = input.length();  // Use input.length() directly for char length
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
            pos += c.length; // Increment pos by the number of chars actually written
        } else {
            // Adjust pos correctly according to consumed code points
            int count = 0;
            while (count < consumed) {
                int cp = Character.codePointAt(input, pos);
                pos += Character.charCount(cp);
                count++;
            }
        }
    }
}
