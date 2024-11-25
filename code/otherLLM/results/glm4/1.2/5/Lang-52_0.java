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

        // Handle non-printable ASCII characters
        if (ch < 32) {
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
                default:
                    // All other non-printable characters below 32 are escaped as \uXXXX
                    out.write("\\u" + String.format("%04x", ch));
                    break;
            }
        } else if (ch > 0xfffe) {
            // Handle 4 byte Unicode characters
            out.write("\\u" + String.format("%08x", ch));
        } else if (ch > 0xff) {
            // Handle 3 byte Unicode characters
            out.write("\\u" + String.format("%06x", ch));
        } else if (ch > 0x7f) {
            // Handle 2 byte Unicode characters
            out.write("\\u" + String.format("%04x", ch));
        } else {
            // Handle printable ASCII characters that need escaping
            switch (ch) {
                case '\'':
                    if (escapeSingleQuote) {
                        out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '"':
                    out.write("\\\"");
                    break;
                case '\\':
                    out.write("\\\\");
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }
}

private static String hex(int value) {
    return Integer.toHexString(value & 0xFF);
}