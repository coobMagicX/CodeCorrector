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
        int charCount = Character.charCount(codePoint);
        
        // try to translate using superclass's translate method which understands Supplementary Characters
        int consumed = super.translate(input, pos, out);

        if (consumed == 0) {
            // If no characters are consumed by the super translate method, handle character manually
            char[] c = Character.toChars(codePoint);
            out.write(c);
            pos += charCount;
        } else {
            // otherwise, increment pos by the number of character units processed by translate
            pos += consumed;
        }
    }
}
