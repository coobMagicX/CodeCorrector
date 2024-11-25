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
        } else {
            // contract with translators is that they have to understand codepoints 
            // and they just took care of a surrogate pair
            int codepoint = Character.codePointAt(input, pos);
            StringEscapeUtils.escapeXml(out, (char) codepoint);
            pos += Character.charCount(codepoint);
        }
        pos++;
    }
}

public final int translate(CharSequence input, int start, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return 0;
    }
    int consumed = 0;
    int codepoint = Character.codePointAt(input, start);
    StringEscapeUtils.escapeXml(out, (char) codepoint);
    consumed += Character.charCount(codepoint);
    return consumed;
}

// Assuming this method is already implemented correctly
public static void escapeXml(Writer out, char c) throws IOException {
    // implementation of XML escaping logic
}