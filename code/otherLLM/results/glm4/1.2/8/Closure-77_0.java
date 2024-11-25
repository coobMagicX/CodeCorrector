static String strEscape(String s, char quote,
                        String doublequoteEscape,
                        String singlequoteEscape,
                        String backslashEscape,
                        CharsetEncoder outputCharsetEncoder) {
  StringBuilder sb = new StringBuilder(s.length() + 2);
  sb.append(quote);
  for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);
    int codePoint = Character.codePointAt(s, i); // Get the actual Unicode code point
    if (Character.isSurrogate(c)) {
      i++; // Move to the next character after a surrogate pair
    }
    switch (c) {
      case '\n': sb.append("\\n"); break;
      case '\r': sb.append("\\r"); break;
      case '\t': sb.append("\\t"); break;
      case '\\': sb.append(backslashEscape); break;
      case '\"': sb.append(doublequoteEscape); break;
      case '\'': sb.append(singlequoteEscape); break;
      // ... other cases remain unchanged ...
      default:
        if (outputCharsetEncoder != null) {
          if (outputCharsetEncoder.canEncode(c)) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(sb, codePoint);
          }
        } else {
          appendHexJavaScriptRepresentation(sb, codePoint);
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}