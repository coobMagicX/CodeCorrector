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
          sb.append("\\u003E"); // Unicode escape for >
        } else {
          sb.append(c);
        }
        break;
      case '<':
        if (i + 4 < s.length() && s.substring(i, i + 5).equals("<!--")) {
          sb.append("\\u003C\\u0021--\\u003E"); // Unicode escape for <!--
        } else if (s.regionMatches(true, i + 1, "/script", 0,
                                   "/script".length())) {
          sb.append("\\u003C\\/\\u0073\\u0063\\u0072\\u0069\\u0070"); // Unicode escape for </script
        } else {
          sb.append(c);
        }
        break;
      default:
        if (outputCharsetEncoder != null) {
          if (outputCharsetEncoder.canEncode(c)) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(sb, c);
          }
        } else {
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

// Additional method needed for Unicode escaping
private static void appendHexJavaScriptRepresentation(StringBuilder sb, char c) {
  String hex = Integer.toHexString(c);
  if (hex.length() == 1) {
    sb.append("\\u00"); // One more leading zero for single digit hex values
  }
  sb.append("\\u").append(hex);
}