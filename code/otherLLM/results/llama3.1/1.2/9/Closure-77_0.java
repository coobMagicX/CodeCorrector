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
        if (i >= 2 &&
            ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
             (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']'))) {
          sb.append("\\>");
        } else {
          // Corrected handling for Unicode escapes
          if (i >= 2 && s.startsWith("\\u", i)) {
            String hex = s.substring(i, i + 6);
            try {
              int codePoint = Integer.parseInt(hex, 16);
              sb.appendCodePoint(codePoint);
            } catch (NumberFormatException e) {
              // If the hexadecimal value is invalid, append it as a literal string
              sb.append("\\u").append(hex);
            }
          } else {
            sb.append(c);
          }
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
            appendHexJavaScriptRepresentation(sb, c);
          }
        } else {
          if (c == '\0') {  // Corrected handling for the null character
            sb.append("\\0");
          } else if (c > 0x1f && c <= 0x7f) {
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