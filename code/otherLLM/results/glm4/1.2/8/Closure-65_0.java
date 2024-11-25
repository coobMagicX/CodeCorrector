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
          sb.append(c);
        }
        break;
      case '<':
        if (i + 7 < s.length() && s.substring(i, i + 8).equals("</script>")) {
          sb.append("<\\/");
        } else if (i + 4 < s.length() && s.substring(i, i + 5).equals("<!--")) {
          sb.append("<\\!");
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
          if (c > 0x1f && c < 0x7f) {
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

private static void appendHexJavaScriptRepresentation(StringBuilder sb, char ch) {
  String hex = Integer.toHexString(ch).toUpperCase();
  if (hex.length() == 1) {
    sb.append("\\u00");
  } else if (hex.length() == 2) {
    sb.append("\\u0");
  }
  sb.append(hex);
}