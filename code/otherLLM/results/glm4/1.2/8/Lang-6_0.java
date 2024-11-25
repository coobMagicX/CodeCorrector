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
        
        // Check if the code point is a high surrogate
        if (Character.isHighSurrogate(codePoint)) {
            if (pos + 1 >= len || !Character.isLowSurrogate(Character.charAt(input, pos + 1))) {
                throw new IllegalArgumentException("Invalid surrogate pair");
            }
            // Write the actual characters of the surrogate pair to the writer
            char[] surrogates = Character.toChars(codePoint);
            out.write(surrogates);
            pos += 2; // Move past the high and low surrogate
        } else {
            // If it's not a high surrogate, simply write the character
            if (Character.isSurrogate(codePoint)) {
                throw new IllegalArgumentException("Invalid code point: " + codePoint);
            }
            char[] c = Character.toChars(codePoint);
            out.write(c);
            pos += c.length;
        }
    }
}