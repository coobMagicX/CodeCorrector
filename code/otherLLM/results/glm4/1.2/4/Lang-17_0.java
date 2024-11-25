public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }

    // Initialize a Translator with the necessary escaping rules.
    CharSequenceTranslator translator = this.with(/* any required translators here */);

    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length());
    while (pos < len) {
        int consumed = translator.translate(input, pos, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, pos));
            out.write(c);
        } else {
            // The translator has handled the surrogate pair, now move forward correctly.
            pos += Character.charCount(Character.codePointAt(input, pos));
        }
        pos++;
    }
}