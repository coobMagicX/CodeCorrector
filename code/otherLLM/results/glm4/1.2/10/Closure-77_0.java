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
                // Check for -->, ]]> or any other sequences before >
                if (i >= 2 && ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
                               (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']') ||
                               s.regionMatches(true, i + 1, "-->", 0, 3) ||
                               s.regionMatches(true, i + 1, "]]>", 0, 3))) {
                    sb.append("\\>");
                } else {
                    sb.append(c);
                }
                break;
            case '<':
                // Check for </script> or <!--
                if (i >= 9 && s.startsWith("</script>", i)) {
                    sb.append("<\\/script>");
                    i += 8; // Skip the next 8 characters, as we already handled them
                } else if (i >= 4 && s.startsWith("<!--", i)) {
                    sb.append("<\\!\\-\\-");
                    i += 3; // Skip the next 3 characters, as we already handled them
                } else {
                    sb.append(c);
                }
                break;
            default:
                // If we're given an outputCharsetEncoder, then check if the character can be represented in this character set.
                if (outputCharsetEncoder != null) {
                    if (!outputCharsetEncoder.canEncode(c)) {
                        appendHexJavaScriptRepresentation(sb, c);
                    } else {
                        sb.append(c);
                    }
                } else {
                    // No charsetEncoder provided - pass straight latin characters through, and escape the rest.
                    if (c > 0x1f && c <= 0x7f) {
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

private static void appendHexJavaScriptRepresentation(Appendable out, char c)
        throws IOException {
    if (Character.isSurrogate(c)) {
        // Handle supplementary unicode values which are not representable in
        // javascript. We deal with these by escaping them as two 4B sequences.
        int codePoint = Character.toCodePoint(c, Character.toLowerCase((char) s.charAt(i + 1)));
        out.append("\\u");
        for (int i = 0; i < 8 - Integer.toString(codePoint, 16).length(); i++) {
            out.append('0');
        }
        out.append(Integer.toHexString(codePoint));
    } else {
        // Regular characters.
        out.append("\\u").append(Integer.toHexString(c)).append("FF");
    }
}