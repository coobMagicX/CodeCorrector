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
            case '\n': sb.append("\\n"); break;
            case '\r': sb.append("\\r"); break;
            case '\t': sb.append("\\t"); break;
            case '\\': sb.append(backslashEscape); break;
            case '\"': sb.append(doublequoteEscape); break;
            case '\'': sb.append(singlequoteEscape); break;
            case '>':
                if (i >= 2) {
                    // Break --> into --\> or ]]> into ]]\>
                    if ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
                        (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']')) {
                        sb.append("\\>");
                    } else {
                        sb.append(c);
                    }
                }
                break;
            case '<':
                // Break </script> into <\/script
                final String END_SCRIPT = "/script";

                // Break <!-- into <\!--
                final String START_COMMENT = "!--";

                if (s.regionMatches(true, i + 1, END_SCRIPT, 0,
                                    END_SCRIPT.length())) {
                    sb.append("<\\");
                } else if (s.regionMatches(false, i + 1, START_COMMENT, 0,
                                           START_COMMENT.length())) {
                    sb.append("<\\");
                } else {
                    sb.append(c);
                }
                break;
            default:
                // If we're given an outputCharsetEncoder, then check if the
                // character can be represented in this character set.
                if (outputCharsetEncoder != null) {
                    if (outputCharsetEncoder.canEncode(c)) {
                        sb.append(c);
                    } else {
                        // Unicode-escape the character.
                        appendHexJavaScriptRepresentation(sb, c);
                    }
                } else {
                    // No charsetEncoder provided - pass straight latin characters
                    // through, and escape the rest.  Doing the explicit character
                    // check is measurably faster than using the CharsetEncoder.
                    if (c >= 0x20 && c <= 0x7E) { // printable ASCII range excluding newline and carriage return
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

// Helper method to append the hexadecimal JavaScript representation of a character
private static void appendHexJavaScriptRepresentation(StringBuilder sb, char c) {
    // Convert character to its hexadecimal string representation and escape
    String hex = Integer.toHexString(c).toUpperCase();
    sb.append("\\u").append(hex);
}