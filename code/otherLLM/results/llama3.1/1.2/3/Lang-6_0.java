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
            pos += Character.charCount(Character.codePointAt(input, pos)); // Correct position increment
            continue;
        }
        for (int pt = 0; pt < consumed; pt++) {
            if (Character.isHighSurrogate(Character.codePointAt(input, pos))) { // Check for high surrogate
                int lowSurrogateCodePoint = CharactercodePointAt(input, pos + 1);
                out.write(new char[] {(char)lowSurrogateCodePoint});
                pos += 2; // Increment position by 2 for surrogate pair
            } else {
                out.write(Character.toChars(Character.codePointAt(input, pos)));
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }
}