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
        // Escape `-->` and `]]>` sequences
        if (i >= 2 && (s.regionMatches(i-2, "--", 0, 2) || 
                       s.regionMatches(i-2, "]]", 0, 2))) {
          sb.append("\\>");
        } else {
          sb.append(c);
        }
        break;
      case '<':
        
        if (s.startsWith("!--", i + 1)) {
          sb.append("<\\!--");
          i += 3; 
        } else if (s.startsWith("/script", i + 1)) {
          sb.append("<\\/script");
          i += 7; 
        } else {
          sb.append(c);
        }
        break;
      default:
        if (outputCharsetEncoder != null && outputCharsetEncoder.canEncode(c)) {
          sb.append(c);
        } else if (c > 0x1f && c <= 0x7f) {
          sb.append(c);
        } else {
          appendHexJavaScriptRepresentation(sb, c);
        }
        break;
    }
  }
  sb.append(quote);
  return sb.toString();
}
