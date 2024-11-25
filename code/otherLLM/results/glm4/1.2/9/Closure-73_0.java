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
        if (i >= 2 && ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
                       (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']'))) {
          sb.append("\\>");
        } else {
          appendHexJavaScriptRepresentation(sb, c);
        }
        break;
      case '<':
        final String END_SCRIPT = "/script";
        final String START_COMMENT = "!--";

        if (s.regionMatches(true, i + 1, END_SCRIPT, 0,
                            END_SCRIPT.length())) {
          sb.append("<\\");
        } else if (s.regionMatches(false, i + 1, START_COMMENT, 0,
                                   START_COMMENT.length())) {
          sb.append("<\\");
        } else {
          appendHexJavaScriptRepresentation(sb, c);
        }
        break;
      default:
        // If we're given an outputCharsetEncoder, then check if the
        // character can be represented in this character set.
        if (outputCharsetEncoder != null) {
          if (!outputCharsetEncoder.canEncode(c)) {
            appendHexJavaScriptRepresentation(sb, c);
          }
        } else {
          // No charsetEncoder provided - pass straight latin characters through,
          // and escape the rest.  Doing the explicit character check is
          // measurably faster than using the CharsetEncoder.
          if (c > 0x7F || c < 0x20) { // Check for non-ASCII or control characters
            appendHexJavaScriptRepresentation(sb, c);
          } else {
            sb.append(c);
          }
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}

private static void appendHexJavaScriptRepresentation(StringBuilder sb, char c) {
  if (c <= 0x7F && c >= 0x20) { // Visible ASCII characters
    sb.append(c);
  } else {
    sb.append(String.format("\\u%04X", (int) c));
  }
}