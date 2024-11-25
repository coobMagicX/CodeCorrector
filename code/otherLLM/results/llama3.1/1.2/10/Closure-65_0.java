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
      case '\0': sb.append("\\x00"); break;
      case '\n': sb.append("\\n"); break;
      case '\r': sb.append("\\r"); break;
      case '\t': sb.append("\\t"); break;
      case '\\': sb.append(backslashEscape); break;
      case '\"': sb.append(doublequoteEscape); break;
      case '\'': sb.append(singlequoteEscape); break;
      default:
        if (Character.isHighSurrogate(c)) {
          appendHexJavaScriptRepresentation(sb, c);
        } else if (Character.isLowSurrogate(c)) {
          appendHexJavaScriptRepresentation(sb, c);
        } else if (outputCharsetEncoder != null && !outputCharsetEncoder.canEncode(c)) {
          appendHexJavaScriptRepresentation(sb, c);
        } else if ((c > 0x1f && c < 0x7f) || (c == '>' || c == '<')) {
          sb.append(c);
        } else {
          // Unicode-escape the character.
          appendHexJavaScriptRepresentation(sb, c);
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}