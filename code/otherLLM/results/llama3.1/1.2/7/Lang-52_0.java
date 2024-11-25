private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (str == null) {
        throw new NullPointerException("The input string must not be null"); // Added exception for null input
    }
    int sz;
    sz = str.length();
    for (int i = 0; i < sz; i++) {
        char ch = str.charAt(i);

        // handle unicode
        if (ch > 0xfff) {
            out.write("\\u" + hex(ch));
        } else if (ch > 0xff) {
            out.write("\\u0" + hex(ch));
        } else if (ch > 0x7f) {
            out.write("\\u00" + hex(ch));
        } else if (ch < 32) {
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
                    out.write '\\');
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
                default :
                    if (ch > 0xf) {
                        out.write("\\u00" + hex(ch));
                    } else {
                        out.write("\\u000" + hex(ch));
                    }
                    break;
            }
        } else {
            switch (ch) {
                case '\'':
                    if (escapeSingleQuote) {
                      out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '"':
                    out.write '\\';
                    out.write('"');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                default :
                    out.write(ch);
                    break;
            }
        }
    }
}

public static void escapeJavaScript(Writer out, String str) throws IOException {
    try {
        escapeJavaStyleString(out, str, true);
    } catch (NullPointerException e) { // Catch and re-throw NullPointerException
        throw new RuntimeException("Input string is null", e);
    } catch (IllegalArgumentException e) { // Catch and re-throw IllegalArgumentException
        throw new RuntimeException("Writer is null", e);
    }
}