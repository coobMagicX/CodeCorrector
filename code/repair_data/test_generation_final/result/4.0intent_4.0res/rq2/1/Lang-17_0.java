public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = input.length(); // Use input.length() to ensure we're working with the actual length in characters
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            int cp = Character.codePointAt(input, pos);
            char[] c = Character.toChars(cp);
            out.write(c);
            pos += Character.charCount(cp); // Increment pos by the number of Java chars used to represent the codepoint
        } else {
            pos += consumed; // Move the position forward by the number of characters consumed
        }
    }
}