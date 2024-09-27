private void printAndEscape(final CharSequence value, final int offset, final int len, final Appendable out)
throws IOException {
    int start = offset;
    int pos = offset;
    final int end = offset + len;

    final char delim = getDelimiter();
    final char escape = getEscapeCharacter().charValue();

    while (pos < end) {
        char c = value.charAt(pos);
        if (c == CR || c == LF || c == delim || c == escape || c == '\\') {
            // write out segment up until this char
            if (pos > start) {
                out.append(value, start, pos);
            }
            if (c == LF) {
                c = 'n';
            } else if (c == CR) {
                c = 'r';
            } else if (c == '\\') {
                c = '\\'; // escape the backslash character
            }

            out.append(escape);
            out.append(c);

            start = pos + 1; // start on the current char after this one
        }

        pos++;
    }

    // write last segment
    if (pos > start) {
        out.append(value, start, pos);
    }
}