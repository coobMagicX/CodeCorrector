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
            // Get the code point at current position and convert it to char array
            int codePoint = Character.codePointAt(input, pos);
            if (!Character.isSurrogate(codePoint)) {
                // If not a surrogate pair, write the character as is
                out.write(codePoint);
            } else {
                // Handle the surrogate pair by writing the high and low surrogates
                int highSurrogate = Character.highSurrogate(codePoint);
                int lowSurrogate = Character.lowSurrogate(codePoint);

                out.write(highSurrogate);
                out.write(lowSurrogate);
            }
            pos += Character.charCount(codePoint); // Increment position by the number of characters written
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                // Assuming 'translate' method returns the count of code points processed
                int codePoint = Character.codePointAt(input, pos);
                if (!Character.isSurrogate(codePoint)) {
                    out.write(codePoint);
                } else {
                    int highSurrogate = Character.highSurrogate(codePoint);
                    int lowSurrogate = Character.lowSurrogate(codePoint);

                    out.write(highSurrogate);
                    out.write(lowSurrogate);
                }
                pos += Character.charCount(codePoint); // Increment position by the number of characters written
            }
        }
    }
}