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
        char[] c = new char[Character.charCount(Character.codePointAt(input, pos))];
        int count = Character.toCharArray(c, input, pos);
        pos += count;
        if (count == 1) { // single character
            out.write(c[0]);
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                out.write(c[pt]);
            }
        }
    }
}

// ... rest of the code remains the same ...