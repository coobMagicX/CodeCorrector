static String strEscape(String s, char quote,
                        String doublequoteEscape,
                        String singlequoteEscape,
                        String backslashEscape,
                        CharsetEncoder outputCharsetEncoder) {
    StringBuilder sb = new StringBuilder(s.length() + 2);
    sb.append(quote);
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
            case '\0': sb.append("\\0"); break;
            case '\n': sb.append("\\n"); break;
            case '\r': sb.append("\\r"); break;
            case '\t': sb.append("\\t"); break;
            case '\\': sb.append(backslashEscape); break;
            case '\"': sb.append(doublequoteEscape); break;
            case '\'': sb.append(singlequoteEscape); break;
            case '>':
                // Handling ">," which could end a comment or CDATA section prematurely in HTML/JavaScript context.
                if (i >= 2 &&
                    ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
                     (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']'))) {
                    sb.append("\\>");
                } else {
                    sb.append(c);
                }
                break;
            case '<':
                // Handle cases that could potentially break out of JavaScript or HTML like "</script>"
                final String END_SCRIPT = "/script";
                final String START_COMMENT = "!--";
                if (s.regionMatches(true, i + 1, END_SCRIPT, 0, END_SCRIPT.length())) {
                    sb.append("<\\");
                } else if (s.regionMatches(false, i + 1, START_COMMENT, 0, START_COMMENT.length())) {
                    sb.append("<\\");
                } else {
                    sb.append(c);
                }
                break;
            default:
                // Adjusted character ranges and handling while there is no CharsetEncoder and while there is.
                if (outputCharsetEncoder != null) {
                    if (outputCharsetEncoder.canEncode(c)) {
                        sb.append(c);
                    } else {
                        // Encode character using hexadecimal escape
                        appendHexJavaScriptRepresentation(sb, c);
                    }
                } else {
                    if (c >= 0x20 && c < 0x7f) { // Print 'normal' characters directly; exclude 0x7f
                        sb.append(c);
                    } else {
                        
                        appendHexJavaScriptRepresentation(sb, c);
                    }
                }
        }
    }
    sb.append(quote);
    return sb.toString();
}


private static void appendHexJavaScriptRepresentation(StringBuilder sb, int c) {
    sb.append("\\u");
    sb.append(String.format("%04X", (int) c));
}
