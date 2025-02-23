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
        
        
        if (ch > 0xfff) {
            out.write("\\u" + Integer.toHexString(ch));
        } else if (ch > 0xff) {
            out.write("\\u0" + Integer.toHexString(ch));
        } else if (ch > 0x7f) {
            out.write("\\u00" + Integer.toHexString(ch));
        } else {
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
                case '"':
                    out.write('\\');
                    out.write('"');
                    break;
                case '\'':
                    if (escapeSingleQuote) out.write('\\');
                    out.write('\'');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                case '/':
                    out.write('\\');  
                    out.write('/');
                    break;
                default:
                    if (ch < 32) {
                        
                        if (ch > 0xf) {
                            out.write("\\u00" + Integer.toHexString(ch));
                        } else {
                            out.write("\\u000" + Integer.toHexString(ch));
                        }
                    } else {
                        out.write(ch);
                    }
                    break;
            }
        }
    }
}
