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
      case '\0': sb.append("\\u0000"); break;
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
          appendHexJavaScriptRepresentation(c, sb);
        }
        break;
      case '<':
        if (s.regionMatches(true, i + 1, "/script", 0,
                            "/script".length())) {
          sb.append("<\\");
        } else if (s.regionMatches(false, i + 1, "!--", 0,
                                   "!--".length())) {
          sb.append("<\\");
        } else {
          appendHexJavaScriptRepresentation(c, sb);
        }
        break;
      default:
        if (outputCharsetEncoder != null) {
          if (outputCharsetEncoder.canEncode(c)) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(c, sb);
          }
        } else {
          if (c > 0x1f && c <= 0x7f) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(c, sb);
          }
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}