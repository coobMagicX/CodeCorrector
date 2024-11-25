public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length());
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
            // Increment pos by the number of code units in the code point
            pos += Character.charCount(Character.codePointAt(input, pos));
        } else {
            // Adjust pos based on the consumed characters
            for (int pt = 0; pt < consumed; pt++) {
                int cp = Character.codePointAt(input, pos);
                out.write(calculateEscapedCodePoint(cp));
                // Increment pos by the number of code units in the code point
                pos += Character.charCount(cp);
            }
        }
    }
}

// Helper method to escape a code point if necessary (assuming this is needed for the context)
private int calculateEscapedCodePoint(int cp) {
    if (cp <= 0x7F || cp >= 0x10FFFF) {
        return cp; // No escaping required
    }
    // Escaping logic would go here, but it's not provided in the original context.
    // Placeholder for actual escaping implementation.
    return cp;
}