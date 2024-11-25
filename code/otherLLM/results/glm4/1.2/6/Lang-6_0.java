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
            // Directly write the character without splitting into code points
            // since we're already handling it correctly with surrogate pairs.
            out.write(Character.toString(input.charAt(pos)));
            pos++;
            continue;
        }
        for (int pt = 0; pt < consumed; pt++) {
            int codePointAtPos = Character.codePointAt(input, pos + pt);
            if ((codePointAtPos & 0xFFFFF800) != 0x010000) { // Check if it's a high surrogate
                char[] c = Character.toChars(codePointAtPos);
                out.write(c);
                pos += Character.charCount(codePointAtPos);
            } else {
                // It's a low surrogate, so we need to handle the pair.
                int highSurrogate = codePointAtPos - 0x0100;
                char[] highSurrogateChars = Character.toChars(highSurrogate);
                out.write(highSurrogateChars); // Write the high part of the surrogate pair
                pos += Character.charCount(codePointAtPos);
            }
        }
    }
}