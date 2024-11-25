private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (str == null) {
        return;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
        char ch = str.charAt(i);

        // handle unicode
        if (ch > 0xfff) {
            out.write("\\u" + hex(ch));
        } else if (ch > 0xff) {
            out.write("\\u0" + hex(ch));
        } else if (ch > 0x7f) {
            out.write("\\u00" + hex(ch));
        } else if (ch < 32) { // Control characters
            switch (ch) {
                case '\b':
                    out.write('\\');
                    out.write('b');
                    break;
                case '\n':
                    out.write('\\');
                    out.write('n');
                    break;
                case '\t':
                    out.write('\\');
                    out.write('t');
                    break;
                case '\f':
                    out.write('\\');
                    out.write('f');
                    break;
                case '\r':
                    out.write('\\');
                    out.write('r');
                    break;
                default: // Handle other control characters less than 0x10
                    if (ch > 0xf) {
                        out.write("\\u00" + hex(ch));
                    } else {
                        out.write("\\u000" + hex(ch));
                    }
                    break;
            }
        } else { // Visible and printable characters except for quotes and backslash
            switch (ch) {
                case '\'':
                    if (escapeSingleQuote) {
                        out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '"':
                    out.write('\\');
                    out.write('"');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }
}

private static String hex(int i) {
    return Integer.toHexString(i).toUpperCase();
}