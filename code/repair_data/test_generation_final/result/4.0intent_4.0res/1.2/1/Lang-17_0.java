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
        int codePoint = Character.codePointAt(input, pos);
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(codePoint);
            out.write(c);
            pos += Character.charCount(codePoint); // Correctly increment pos based on the number of Java 'char' values used to represent the codePoint
        } else {
            pos += consumed; // Adjust pos by the number of characters processed by translate
        }
    }
}