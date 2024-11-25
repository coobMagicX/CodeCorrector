private static void escapeJavaScript(Writer out, String str) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (str == null) {
        return;
    }

    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);
        switch (ch) {
            case '\b':
                out.write("\\b");
                break;
            case '\f':
                out.write("\\f");
                break;
            case '\n':
                out.write("\\n");
                break;
            case '\r':
                out.write("\\r");
                break;
            case '\t':
                out.write("\\t");
                break;
            case '&':
                out.write("&amp;");
                break;
            case '<':
                out.write("&lt;");
                break;
            case '>':
                out.write("&gt;");
                break;
            case '\'':
                out.write("\'");
                break;
            case '\"':
                out.write("\"");
                break;
            case '/':
                out.write("\\/");
                break;
            default:
                if (ch <= 0x1F || ch >= 0x7F && ch != '\u2028' && ch != '\u2029') {
                    out.write("&#" + (int) ch + ";");
                } else {
                    out.write(ch);
                }
                break;
        }
    }
}