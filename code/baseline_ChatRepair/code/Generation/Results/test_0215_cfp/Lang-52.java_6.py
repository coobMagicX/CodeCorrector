private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote, boolean escapeSlash) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (str == null) {
        return;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
        char ch = str.charAt(i);

        switch (ch) {
            case '\b':
                out.write("\\b");
                break;
            case '\n':
                out.write("\\n");
                break;
            case '\t':
                out.write("\\t");
                break;
            case '\f':
                out.write("\\f");
                break;
            case '\r':
                out.write("\\r");
                break;
            case '"':
                out.write("\\\"");
                break;
            case '\'':
                if (escapeSingleQuote) {
                    out.write("\\'");
                }
                break;
            case '\\':
                out.write("\\\\");
                break;
            case '/':
                if (escapeSlash) {
                    out.write("\\/");
                }
                break;
            default:
                // handle non-control characters > 0x7F
                if (ch > 0x7f) {
                    out.write(String.format("\\u%04x", (int) ch));
                } else {
                    out.write(ch);
                }
                break;
        }
    }
}
